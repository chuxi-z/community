package com.firstpro.community.Controller;


import com.firstpro.community.dto.CommentCreateDTO;
import com.firstpro.community.dto.CommentDTO;
import com.firstpro.community.dto.ResultDTO;
import com.firstpro.community.enums.CommentTypeEnum;
import com.firstpro.community.exception.CustomizeErrorCode;
import com.firstpro.community.model.Comment;
import com.firstpro.community.model.User;
import com.firstpro.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

//    @Autowired
//    @Resource
//    private CommentMapper commentMapper;

    @Autowired
    @Resource
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setLikeCount(0L);
        comment.setCommentator(user.getId());
//        commentMapper.insert(comment);
        commentService.insert(comment, user);
        return ResultDTO.passOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod. GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.passOf(commentDTOS);
    }

}
