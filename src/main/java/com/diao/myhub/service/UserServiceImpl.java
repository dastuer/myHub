package com.diao.myhub.service;

import com.diao.myhub.mapper.UserMapper;
import com.diao.myhub.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    public void setMapper(UserMapper mapper) {
        this.mapper = mapper;
    }
    UserMapper mapper;

    @Override
    public int addUser(User user) {
        return mapper.addUser(user);
    }

    @Override
    public User selectUserByToken(String token) {
        return mapper.selectUserByToken(token);
    }

    @Override
    public int updateToken(Integer id,String token) {
       return mapper.updateToken(id,token);
    }

    @Override
    public int updateUnread(Integer id) {
    return     mapper.updateUnread(id);
    }

    @Override
    public int readAll(Integer id) {
        return mapper.readAll(id);
    }

    @Override
    public int updateUnreadDe(Integer id) {
        return mapper.updateUnreadDe(id);
    }

    public User getUserById(int id){
        return mapper.getUserById(id);
    }

    @Override
    public User getUserByAccountId(String id) {
        return mapper.getUserByAccountId(id);
    }
}
