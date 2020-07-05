package com.firstpro.community.service;


import com.firstpro.community.dto.PaginationDTO;
import com.firstpro.community.dto.QuestionDTO;
import com.firstpro.community.exception.CustomizeException;
import com.firstpro.community.mapper.QuestionMapper;
import com.firstpro.community.mapper.UserMapper;
import com.firstpro.community.model.Question;
import com.firstpro.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    @Resource
    UserMapper userMapper;

    @Autowired
    @Resource
    QuestionMapper questionMapper;

    public PaginationDTO list(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
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
        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for(Question question : questions){
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.countByUserId(userId);
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
        List<Question> questions = questionMapper.listByUserId(userId, offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for(Question question : questions){
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        //error detective
        if(question == null){
            throw new CustomizeException("The question is out of index...");
        }
        QuestionDTO questionDTO = new QuestionDTO();
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        BeanUtils.copyProperties(question, questionDTO);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){//new
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }
        else{//update
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.update(question);
        }

    }
}
