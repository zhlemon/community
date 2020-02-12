package com.learn.majiang.service;

import com.learn.majiang.mapper.QuestionMapper;
import com.learn.majiang.model.Question;
import com.learn.majiang.model.QuestionExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotQuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    public List<Question> findHotQuestion() {
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("viewcount DESC");
        List<Question> questions = questionMapper.selectByExample(questionExample);
        List<Question> hot4questions = questions.stream()
                .limit(4)
                .collect(Collectors.toList());
        return hot4questions;
    }
}
