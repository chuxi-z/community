package com.firstpro.community.service;


import com.firstpro.community.dto.PaginationDTO;
import com.firstpro.community.dto.QuestionDTO;
import com.firstpro.community.exception.CustomizeErrorCode;
import com.firstpro.community.exception.CustomizeException;
import com.firstpro.community.mapper.QuestionExtMapper;
import com.firstpro.community.mapper.QuestionMapper;
import com.firstpro.community.mapper.UserMapper;
import com.firstpro.community.model.Question;
import com.firstpro.community.model.QuestionExample;
import com.firstpro.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    @Resource
    UserMapper userMapper;

    @Autowired
    @Resource
    QuestionMapper questionMapper;

    @Autowired
    @Resource
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());
        Integer totalPage;
        if(totalCount%size == 0){
            totalPage = totalCount/size;
        }
        else{
            totalPage = totalCount/size + 1;
        }

        if(page < 1){
            page = 1;
        }
        else if(page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage, page);

        Integer offset = size*(page-1);
//        List<Question> questions = questionMapper.list(offset, size);

        QuestionExample orderQuestion = new QuestionExample();
        orderQuestion.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(orderQuestion, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for(Question question : questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
//        Integer totalCount = questionMapper.countByUserId(userId);

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);
        Integer totalPage;
        if(totalCount%size == 0){
            totalPage = totalCount/size;
        }
        else{
            totalPage = totalCount/size + 1;
        }

        if(page < 1){
            page = 1;
        }
        else if(page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage, page);

        Integer offset = size*(page-1);
//        List<Question> questions = questionMapper.listByUserId(userId, offset, size);

        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for(Question question : questions){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);

        //error detective
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        BeanUtils.copyProperties(question, questionDTO);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){//new
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
//            question.setGmtCreate(System.currentTimeMillis());
//            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }
        else{//update
            question.setGmtModified(System.currentTimeMillis());
            Question updataQuestion = new Question();
            updataQuestion.setGmtModified(System.currentTimeMillis());
            updataQuestion.setTitle(question.getTitle());
            updataQuestion.setTag(question.getTag());
            updataQuestion.setDescription(question.getDescription());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updataQuestion, example);
            if(updated != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }

    }

    public void  incView(Long id) {
//        Question question = questionMapper.selectByPrimaryKey(id);
//        Question updateQuestion = new Question();
//        updateQuestion.setViewCount(question.getViewCount()+1);
//
//        QuestionExample questionExample = new QuestionExample();
//        questionExample.createCriteria()
//                .andIdEqualTo(id);

        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
//        questionMapper.updateByExampleSelective(updateQuestion, questionExample);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if(StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }

        String[] tags = StringUtils.split(questionDTO.getTag(), ",");
        String regexp = Arrays.stream(tags).collect(Collectors.joining("|"));

        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexp);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOs = questions.stream().map(q -> {
            QuestionDTO quesDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, quesDTO);
            return quesDTO;
        }).collect(Collectors.toList());

        return questionDTOs;
    }
}
