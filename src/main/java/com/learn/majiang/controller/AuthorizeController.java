package com.learn.majiang.controller;

import com.learn.majiang.dto.AccessTokenDTO;
import com.learn.majiang.dto.GithubUser;
import com.learn.majiang.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    GithubProvider githubProvider;

    /**
     * 接收git的code
     *
     * @return
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id("6db3c5a336f42c783d22");
        accessTokenDTO.setClient_secret("1ddcfa3d9a4c5a3488b2119a13974882dfde7cd8");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("http://localhost:8887/callback");
        accessTokenDTO.setState(state);
        String token = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(token);
        System.out.println(user);
        return "index";
    }
}
