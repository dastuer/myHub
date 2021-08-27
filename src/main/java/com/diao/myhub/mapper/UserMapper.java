package com.diao.myhub.mapper;

import com.diao.myhub.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Insert("insert into user(name,account_id,token,gmt_create,gmt_modify,bio,avatar_url) " +
            "value (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModify},#{bio},#{avatarUrl})")
    int addUser(User user);
    @Select("select * from user where token=#{token}")
    User selectUserByToken(String token);
    @Select("select * from myhub.user where id=#{id}")
    User getUserById(int id);
    @Select("select * from myhub.user where account_id=#{id}")
    User getUserByAccountId(String id);
    @Update("update myhub.user set token = #{token} where id = #{id}")
    int updateToken(Integer id, String token);
    @Update("update myhub.user set unread = unread + 1 where id = #{id}")
    int updateUnread(Integer id);
    @Update("update myhub.user set unread = 0 where id = #{id}")
    int readAll(Integer id);
    @Update("update myhub.user set unread = unread - 1 where id = #{id}")
    int updateUnreadDe(Integer id);
}
