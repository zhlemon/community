package com.learn.majiang.service;

import com.learn.majiang.mapper.UserMapper;
import com.learn.majiang.model.User;
import com.learn.majiang.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        //数据库中的user(dbUser)
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size()==0){
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            //更新
            User dbUser=users.get(0);
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setAvatarurl(user.getAvatarurl());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());


            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(dbUser, example);
        }
    }
}
