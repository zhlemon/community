package com.learn.majiang.mapper;

import com.learn.majiang.model.Question;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
}