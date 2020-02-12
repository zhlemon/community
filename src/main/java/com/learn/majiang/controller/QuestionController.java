package com.learn.majiang.controller;

import com.learn.majiang.dto.CommentDto;
import com.learn.majiang.dto.QuestionDto;
import com.learn.majiang.enums.CommentTypeEnum;
import com.learn.majiang.enums.NotificationEnum;
import com.learn.majiang.mapper.CommentExtMapper;
import com.learn.majiang.mapper.NotificationMapper;
import com.learn.majiang.model.Notification;
import com.learn.majiang.model.NotificationExample;
import com.learn.majiang.service.CommentService;
import com.learn.majiang.service.NotificationService;
import com.learn.majiang.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @Autowired
    private NotificationMapper notificationMapper;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Integer id,
                           Model model
    ) {
        Map<Integer, Integer> map = new HashMap<>();
        QuestionDto questionDto = questionService.getById(id);
        List<QuestionDto> relatedQuestionList = commentService.findRelated(questionDto);
        questionService.incView(id);
        List<CommentDto> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION.getType());
        for (CommentDto comment : comments) {
            Integer commentId = comment.getId();
            Integer countComment = commentExtMapper.countComment(commentId);
            map.put(comment.getId(), countComment);
        }
        model.addAttribute("question", questionDto);
        model.addAttribute("comments", comments);
        model.addAttribute("commentCount", map);
        model.addAttribute("relatedQuestions", relatedQuestionList);
        return "question";
    }

    /**
     * 如果查看了61的question就算评论了多次 也全部算看过了
     *
     * @param questionId
     */
    @GetMapping("/question/read/{questionId}")
    public String readQuestion(@PathVariable("questionId") Long questionId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andOuteridEqualTo(questionId);
        List<Notification> notifications = notificationMapper.selectByExample(notificationExample);
        notifications.forEach(notification -> notification.setStatus(1));
        notifications.forEach(notification -> notificationMapper.updateByPrimaryKey(notification));
        return "redirect:/question/"+questionId;
    }
}
