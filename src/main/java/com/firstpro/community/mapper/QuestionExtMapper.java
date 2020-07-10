package com.firstpro.community.mapper;

import com.firstpro.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question question);
    int incCommentCount(Question question);

    List<Question> selectRelated(Question question);
}