package com.learn.majiang.controller;

import com.learn.majiang.dto.PageDto;
import com.learn.majiang.exception.CustomizeErrorCode;
import com.learn.majiang.exception.CustomizeException;
import com.learn.majiang.model.Question;
import com.learn.majiang.model.User;
import com.learn.majiang.service.HotQuestionService;
import com.learn.majiang.service.NotificationService;
import com.learn.majiang.service.QuestionService;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 首页
 */
@Controller
public class IndexController {

    @Autowired
    QuestionService questionService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    private HotQuestionService hotQuestionService;

    @GetMapping("/")
    public String index(
            Model model,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            HttpServletRequest request
    ) {
        User user = (User) request.getSession().getAttribute("user");
        //未登录
        if (user == null) throw new CustomizeException(CustomizeErrorCode.USER_HAS_NOT_LOGIN_IN);
        Integer notificationNum = notificationService.getNotificationNum(user.getId());
        PageDto pageDtoInfo = questionService.list(page, size);
        List<Question> hotQuestion = hotQuestionService.findHotQuestion();
        model.addAttribute("pageDtoInfo", pageDtoInfo);
        //通知数量
        model.addAttribute("notificationNum", notificationNum);
        //热门问题
        model.addAttribute("hotQuestion", hotQuestion);
        return "index";
    }

    //搜索问题
    @GetMapping("/search")
    public String search(@RequestParam(name = "questionName") String questionName,
                         HttpServletRequest request,
                         @RequestParam(name = "page", defaultValue = "1") Integer page,
                         @RequestParam(name = "size", defaultValue = "5") Integer size,
                         Model model) {
        User user = (User) request.getSession().getAttribute("user");
        Integer notificationNum = notificationService.getNotificationNum(user.getId());
        PageDto pageDtoInfo = questionService.list(page, size, questionName);
        model.addAttribute("pageDtoInfo", pageDtoInfo);
        model.addAttribute("notificationNum", notificationNum);
        return "index";

    }

}
