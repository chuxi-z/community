package com.firstpro.community.mapper;

import com.firstpro.community.model.Question;
import com.firstpro.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question question);
}