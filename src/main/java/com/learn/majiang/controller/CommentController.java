package com.learn.majiang.controller;

import com.learn.majiang.dto.CommentCreateDto;
import com.learn.majiang.dto.CommentDto;
import com.learn.majiang.dto.ResultDto;
import com.learn.majiang.enums.CommentTypeEnum;
import com.learn.majiang.exception.CustomizeErrorCode;
import com.learn.majiang.mapper.CommentMapper;
import com.learn.majiang.model.Comment;
import com.learn.majiang.model.User;
import com.learn.majiang.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public ResultDto post(@RequestBody CommentCreateDto commentCreateDto,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            return ResultDto.errorOf(CustomizeErrorCode.USER_HAS_NOT_LOGIN_IN);
        }

        if(commentCreateDto==null|| StringUtils.isBlank(commentCreateDto.getContent())){
            return ResultDto.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentid(commentCreateDto.getParentId());
        comment.setContent(commentCreateDto.getContent());
        comment.setType(commentCreateDto.getType());
        comment.setLikecount(0L);
        comment.setGmtmodified(System.currentTimeMillis());
        comment.setGmtcreate(System.currentTimeMillis());
        comment.setCommentor(user.getAccountId());

        commentService.insert(comment);
        return ResultDto.okOf();
    }

    //回复评论
    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDto<List<CommentDto>> comments(@PathVariable(name = "id") Integer id){
        List<CommentDto> commentDtos = commentService.lisyByTargetId(id, CommentTypeEnum.COMMENT.getType());
        return ResultDto.okOf(commentDtos);
    }
}
