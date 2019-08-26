package com.learn.majiang.controller;

import com.learn.majiang.dto.CommentDto;
import com.learn.majiang.dto.ResultDto;
import com.learn.majiang.exception.CustomizeErrorCode;
import com.learn.majiang.mapper.CommentMapper;
import com.learn.majiang.model.Comment;
import com.learn.majiang.model.User;
import com.learn.majiang.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDto commentDto,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return ResultDto.errorOf(CustomizeErrorCode.USER_HAS_NOT_LOGIN_IN);
        }

        Comment comment = new Comment();
        comment.setParentid(commentDto.getParentId());
        comment.setContent(commentDto.getContent());
        comment.setType(commentDto.getType());
        comment.setLikecount(0L);
        comment.setGmtmodified(System.currentTimeMillis());
        comment.setGmtcreate(System.currentTimeMillis());
        comment.setCommentor(user.getAccountId());

        commentService.insert(comment);
        return ResultDto.okOf();
    }
}
