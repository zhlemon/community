package com.learn.majiang.service;

import com.learn.majiang.dto.CommentCreateDto;
import com.learn.majiang.dto.CommentDto;
import com.learn.majiang.enums.CommentTypeEnum;
import com.learn.majiang.exception.CustomizeErrorCode;
import com.learn.majiang.exception.CustomizeException;
import com.learn.majiang.mapper.CommentMapper;
import com.learn.majiang.mapper.QuestionExtMapper;
import com.learn.majiang.mapper.QuestionMapper;
import com.learn.majiang.mapper.UserMapper;
import com.learn.majiang.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentid() == null || comment.getParentid() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentid());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);

        } else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentid());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentcount(1);
            questionExtMapper.incCommentCount(question);
        }
    }


    public List<CommentDto> listByQuestionId(Integer id) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParentidEqualTo(id)
                .andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
        List<Comment> comments = commentMapper.selectByExample(example);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        Set<String> commentCreators = comments.stream().map(comment -> comment.getCommentor()).collect(Collectors.toSet());
        List<String> userIds=new ArrayList<>();
        userIds.addAll(commentCreators);

        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getAccountId(), user -> user));

        List<CommentDto> commentDtos = comments.stream().map(comment -> {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(comment,commentDto);
            commentDto.setUser(userMap.get(comment.getCommentor()));
            return commentDto;
        }).collect(Collectors.toList());

        return commentDtos;
    }
}
