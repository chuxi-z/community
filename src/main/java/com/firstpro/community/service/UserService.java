package com.firstpro.community.service;

import com.firstpro.community.mapper.UserMapper;
import com.firstpro.community.model.User;
import com.firstpro.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@Service
public class UserService {
    @Resource
    @Autowired
    private UserMapper userMapper;


    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
//        User dbUser = (User) users;
        if(users.size() == 0){//insert
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }
        else{//update
            User dbUser = users.get(0);
//            dbUser.setGmtModified(System.currentTimeMillis());
//            dbUser.setAvatarUrl(user.getAvatarUrl());
//            dbUser.setName(user.getName());
//            dbUser.setToken(user.getToken());

            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
//            userMapper.update(dbUser);
        }
    }
}
