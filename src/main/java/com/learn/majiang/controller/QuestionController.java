package com.learn.majiang.controller;

import com.learn.majiang.dto.CommentCreateDto;
import com.learn.majiang.dto.CommentDto;
import com.learn.majiang.dto.QuestionDto;
import com.learn.majiang.service.CommentService;
import com.learn.majiang.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//问题详情页面
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id,
                           Model model
    ) {
        QuestionDto questionDto = questionService.getById(id);
        questionService.incView(id);
        List<CommentDto> comments = commentService.listByQuestionId(id);
        model.addAttribute("question", questionDto);
        model.addAttribute("comments", comments);
        return "question";
    }
}
