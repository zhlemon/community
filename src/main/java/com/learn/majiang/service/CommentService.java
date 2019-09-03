package com.learn.majiang.service;

import com.learn.majiang.dto.CommentDto;
import com.learn.majiang.dto.QuestionDto;
import com.learn.majiang.enums.CommentTypeEnum;
import com.learn.majiang.enums.NotificationEnum;
import com.learn.majiang.enums.NotificationTypeEnum;
import com.learn.majiang.exception.CustomizeErrorCode;
import com.learn.majiang.exception.CustomizeException;
import com.learn.majiang.mapper.*;
import com.learn.majiang.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

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

            //创建一个通知
            createNotify(comment, dbComment.getCommentor(), NotificationEnum.REPLY_COMMENT);
        } else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentid());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentcount(1);
            questionExtMapper.incCommentCount(question);
            createNotify(comment,question.getCreator(), NotificationEnum.REPLY_QUESTION);
        }
    }



    //创建通知
    private void createNotify(Comment comment, String receiver, NotificationEnum notificationType) {
        Notification notification = new Notification();
        notification.setNotifier(Long.valueOf(comment.getCommentor()));
        notification.setGmtcreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(Long.valueOf(comment.getParentid()));
        notification.setNotifier(Long.valueOf(comment.getCommentor()));
        notification.setStatus(NotificationTypeEnum.UNREAD.getStatus());
        notification.setReceiver(Long.valueOf(receiver));
        notificationMapper.insert(notification);
    }




    public List<CommentDto> lisyByTargetId(Integer id, Integer type) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParentidEqualTo(id)
                .andTypeEqualTo(type);
        example.setOrderByClause("gmtCreate desc");
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

    public Integer findCommentCount(Integer id) {
        Integer counComment = commentExtMapper.counComment(id);
        return counComment;
    }


    public List<QuestionDto> findRelated(QuestionDto queryDto) {
        if(StringUtils.isBlank(queryDto.getTag())){
            return new ArrayList<>();
        }

        String[] tags = StringUtils.split(queryDto.getTag(), "-");
        String tag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDto.getId());
        question.setTag(tag);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDto> questionDtos = questions.stream().map(q -> {
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(q, questionDto);
            return questionDto;
        }).collect(Collectors.toList());

        return questionDtos;
    }
}
