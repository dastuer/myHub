package com.diao.myhub.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private String token;
    private String accountId;
    private String bio;
    private String avatarUrl;
    private long gmtCreate;
    private long gmtModify;
    private int unread;
}
