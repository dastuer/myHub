package com.diao.myhub.dto;

import com.diao.myhub.model.Question;
import com.diao.myhub.model.User;
import lombok.Data;

@Data
public class MyCommentsDTO {

    private Long id;
    private Long parentId;
    private Integer commenter;
    private Short type;
    private Long gmtCreate;
    private Long gmtModify;
    private Long likeCount;
    private String content;

    private User user;
    private Question question;

}
