package com.learn.majiang.controller;

import com.learn.majiang.dto.PageDto;
import com.learn.majiang.mapper.UserMapper;
import com.learn.majiang.model.User;
import com.learn.majiang.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "2") Integer size
    ) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();

                    //用cookie中的token查询数据库中是否有该用户
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        //如果数据库中有该用户 则将用户放入session中 页面拿到session.user显示
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        } else {
            return "index";
        }

        PageDto pageDtoInfo = questionService.list(page,size);
        model.addAttribute("pageDtoInfo", pageDtoInfo);
        return "index";
    }

    //退出登录
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");
        userMapper.deleteUser(user.getId());

        session.removeAttribute("user");
        return "redirect:/";
    }
}
