package com.learn.majiang.controller;

import com.learn.majiang.dto.AccessTokenDTO;
import com.learn.majiang.dto.GithubUser;
import com.learn.majiang.mapper.UserMapper;
import com.learn.majiang.model.User;
import com.learn.majiang.provider.GithubProvider;
import com.learn.majiang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    /**
     * 接收git的code
     *
     * @return
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String token = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(token);
        if (githubUser != null && githubUser.getId() != null) {
            //登录成功
            //写入数据库user
            User user = new User();
            //自己的token uuid实现
            String token1 = UUID.randomUUID().toString();
            user.setToken(token1);
            user.setName(githubUser.getName());
            //这是git的account_id
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setBio(githubUser.getBio());
            user.setAvatarUrl(githubUser.getAvatar_url());
            //插入或是更新数据库
            userService.createOrUpdate(user);
            //向前端写入cookie
            response.addCookie(new Cookie("token", token1));
            //注意redireact后面跟的是请求url 不是页面的名字
            return "redirect:/";
        } else {
            //登录失败
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
