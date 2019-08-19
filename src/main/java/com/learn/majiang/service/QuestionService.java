package com.learn.majiang.service;

import com.learn.majiang.dto.PageDto;
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

    public PageDto list(Integer page, Integer size) {
        //在service中通过questionMapper userMapper 组合生成一个QuestionDto
        //QuestionDto包含了Question和User
        PageDto pageDto = new PageDto();
        //查记录总数
        Integer totalCount = questionMapper.count();
        pageDto.setPageInfo(totalCount,page,size);

        if(page<1){
            page=1;
        }

        if(page>pageDto.getTotalPage()){
            page=pageDto.getTotalPage();
        }

        //通过page计算offset
        Integer offset = size * (page - 1);
        //offset和size查数据库拿到question
        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDto> questionDtoList = new ArrayList<>();

        //questionlist-->加入user
        for (Question question : questions) {
            User user = userMapper.findUserById(question.getId());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }

        //questionDto变为pageDto
        pageDto.setQuestions(questionDtoList);
        return pageDto;
    }
}
