package com.firstpro.community.mapper;

import com.firstpro.community.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}