package com.learn.majiang.service;

import com.learn.majiang.dto.QuestionDto;
import com.learn.majiang.mapper.QuestionMapper;
import com.learn.majiang.mapper.UserMapper;
import com.learn.majiang.model.Question;
import com.learn.majiang.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    public List<QuestionDto> list() {
        //在service中通过questionMapper userMapper 组合生成一个QuestionDto
        //QuestionDto包含了Question和User
        List<Question> questions = questionMapper.list();
        List<QuestionDto> questionDtoList=new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findUserById(question.getId());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        return questionDtoList;
    }
}
