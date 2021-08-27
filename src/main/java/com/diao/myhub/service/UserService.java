package com.diao.myhub.service;

import com.diao.myhub.model.User;
import io.swagger.models.auth.In;

public interface UserService {
    public int addUser(User user);
    public User getUserById(int id);
    public User getUserByAccountId(String id);
    public User selectUserByToken(String token);
    int updateToken(Integer id,String token);
    int updateUnread(Integer id);
    int readAll(Integer id);

    int updateUnreadDe(Integer id);

}
