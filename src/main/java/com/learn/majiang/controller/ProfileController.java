package com.learn.majiang.controller;

import com.learn.majiang.dto.PageDto;
import com.learn.majiang.dto.ReplyDto;
import com.learn.majiang.mapper.UserMapper;
import com.learn.majiang.model.Comment;
import com.learn.majiang.model.User;
import com.learn.majiang.service.NotificationService;
import com.learn.majiang.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//通知
@Controller
public class ProfileController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profiles/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size) {

        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }


        if ("questions".equals(action)) {
            //如果是我的问题
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            PageDto pageDtoInfo = questionService.list(user.getId(), page, size);
            model.addAttribute("pageDtoInfo", pageDtoInfo);
        } else if ("replies".equals(action)) {
            //如果是最新回复
            //PageDto pageDtoInfo=notificationService.list(user.getId(), page, size);
            Integer notificationNum = notificationService.getNotificationNum(user.getId());
            List<ReplyDto> comments = notificationService.listAllNotifications(user.getId());
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            //model.addAttribute("pageDtoInfo", pageDtoInfo);
            model.addAttribute("comments",comments);
            model.addAttribute("notificationNum",notificationNum);
        }

        return "profile";
    }
}
