package com.learn.majiang.service;

import com.learn.majiang.dto.NotificationDto;
import com.learn.majiang.dto.PageDto;
import com.learn.majiang.dto.QuestionDto;
import com.learn.majiang.mapper.NotificationMapper;
import com.learn.majiang.mapper.UserMapper;
import com.learn.majiang.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    public PageDto list(Integer id, Integer page, Integer size) {

        PageDto<NotificationDto> pageDto = new PageDto<>();

        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(Long.valueOf(id));
        Integer totalCount = (int)notificationMapper.countByExample(example);

        Integer totalPage;
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }

        if (page > totalPage) {
            page = totalPage;
        }

        if (page == 0) {
            page = 1;
        }
        Integer offset = size * (page - 1);

        pageDto.setPageInfo(totalPage, page, size);

        //offset和size查数据库拿到question
        NotificationExample example1 = new NotificationExample();
        example1.createCriteria().andReceiverEqualTo(Long.valueOf(id));
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));

        if(notifications.size()==0){
            return pageDto;
        }
        Set<Integer> disUserId = notifications.stream().map(notify -> notify.getNotifier().intValue()).collect(Collectors.toSet());
        ArrayList<Integer> userIds = new ArrayList<>(disUserId);

        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(u -> u.getId(), u -> u));

        List<NotificationDto> notificationDtos = new ArrayList<>();
        pageDto.setData(notificationDtos);

        return pageDto;
    }
}
