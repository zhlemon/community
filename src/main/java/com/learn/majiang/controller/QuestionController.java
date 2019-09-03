package com.learn.majiang.controller;

import com.learn.majiang.dto.CommentDto;
import com.learn.majiang.dto.QuestionDto;
import com.learn.majiang.enums.CommentTypeEnum;
import com.learn.majiang.mapper.CommentExtMapper;
import com.learn.majiang.model.Question;
import com.learn.majiang.service.CommentService;
import com.learn.majiang.service.QuestionService;
import jdk.nashorn.internal.codegen.ClassEmitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//问题详情页面
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id,
                           Model model
    ) {
        Map<Integer,Integer> map=new HashMap<>();
        QuestionDto questionDto = questionService.getById(id);
        List<QuestionDto> relatedQuestionList=commentService.findRelated(questionDto);
        questionService.incView(id);
        List<CommentDto> comments = commentService.lisyByTargetId(id, CommentTypeEnum.QUESTION.getType());
        for(CommentDto comment:comments){
            Integer commentId = comment.getId();
            Integer countComment = commentExtMapper.counComment(commentId);
            map.put(comment.getId(),countComment);
        }
        model.addAttribute("question", questionDto);
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount",map);
        model.addAttribute("relatedQuestions",relatedQuestionList);
        return "question";
    }
}
