package com.learn.majiang.service;

import com.learn.majiang.dto.ReplyDto;
import com.learn.majiang.mapper.CommentMapper;
import com.learn.majiang.mapper.NotificationMapper;
import com.learn.majiang.mapper.QuestionMapper;
import com.learn.majiang.mapper.UserMapper;
import com.learn.majiang.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    /**
     *
     * @param userId 用户id
     * @return 与之关联的回复
     */
    public List<ReplyDto> listAllNotifications(Integer userId) {
        //当前用户提的问题
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId.toString());
        List<Question> questions = questionMapper.selectByExample(questionExample);
        List<Integer> questionsIds = questions.stream().map(Question::getId).collect(Collectors.toList());

        NotificationExample notificationExample1 = new NotificationExample();
        notificationExample1.createCriteria().andStatusEqualTo(1);
        List<Notification> notifications1 = notificationMapper.selectByExample(notificationExample1);
        List<Integer> readedList = notifications1.stream().map(notification -> notification.getOuterid().intValue()).collect(Collectors.toList());
        questionsIds.removeAll(readedList);

        ArrayList<ReplyDto> replyDtos = new ArrayList<>();
        //通过userId查notification receiver-->notification
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(Long.valueOf(userId)).andStatusEqualTo(0);
        List<Notification> notifications = notificationMapper.selectByExample(notificationExample);


        HashSet<Long> notificationIds = new HashSet<>();
        for (Notification notification : notifications) {

            Long notifier = notification.getNotifier();
            Integer status = notification.getStatus();
            if (status==0){
                notificationIds.add(notifier);
            }

        }


        // 通过comment中的commentor
        ArrayList<Comment> allComments = new ArrayList<>();

        for (Long id : notificationIds) {
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andCommentorEqualTo(id.toString()).andParentidIn(questionsIds);
            List<Comment> comments = commentMapper.selectByExample(commentExample);
            allComments.addAll(comments);
        }

        for (Comment comment : allComments) {
            String commentor = comment.getCommentor();
            String content = comment.getContent();
            Long gmtcreate = comment.getGmtcreate();
            Integer questionId = comment.getParentid();
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(gmtcreate));
            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdEqualTo(commentor);
            List<User> users = userMapper.selectByExample(userExample);
            User user = users.get(0);
            String imageUrl = user.getAvatarurl();
            String commentorName = user.getName();
            ReplyDto replyDto = new ReplyDto();
            replyDto.setCommentorName(commentorName);
            replyDto.setCommentTime(date);
            replyDto.setContent(content);
            replyDto.setImage(imageUrl);
            replyDto.setQuestionId(questionId);
            replyDtos.add(replyDto);
        }
        return replyDtos;

    }

    public Integer getNotificationNum(Integer userId){
        NotificationExample notificationExample = new NotificationExample();
        //status=0表示没有看过这个通知
        notificationExample.createCriteria().andReceiverEqualTo(Long.valueOf(userId)).andStatusEqualTo(0);
        List<Notification> notifications = notificationMapper.selectByExample(notificationExample);
        return notifications.size();
    }
}
