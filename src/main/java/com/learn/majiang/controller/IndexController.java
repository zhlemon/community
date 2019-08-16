package com.learn.majiang.controller;

import com.learn.majiang.mapper.UserMapper;
import com.learn.majiang.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
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
        }else {
            return "index";
        }

        return "index";
    }
}
