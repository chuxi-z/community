package com.firstpro.community.service;

import com.firstpro.community.enums.CommentTypeEnum;
import com.firstpro.community.exception.CustomizeErrorCode;
import com.firstpro.community.exception.CustomizeException;
import com.firstpro.community.mapper.CommentMapper;
import com.firstpro.community.mapper.QuestionExtMapper;
import com.firstpro.community.mapper.QuestionMapper;
import com.firstpro.community.model.Comment;
import com.firstpro.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class CommentService {

    @Autowired
    @Resource
    private CommentMapper commentMapper;

    @Autowired
    @Resource
    private QuestionMapper questionMapper;

    @Autowired
    @Resource
    private QuestionExtMapper questionExtMapper;

    //事务 一个操作失败 整体不执行：防止由于网络抖动或者其它原因 数据对应不上
    @Transactional
    public void insert(Comment comment) {
        if(comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND );
        }

        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND );
        }

        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }
        else{
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }
}
