package com.diao.myhub.dto;

import com.diao.myhub.model.Comment;
import com.diao.myhub.model.Question;
import com.diao.myhub.model.User;
import lombok.Data;

@Data
public class NotifyDTO {
    private Long id;
    private Integer notifier;
    private Integer receiver;
    private Short type;
    private Long gmtCreate;
    private Short status;
    private String outerTitle;
    private User user;
    Comment comment;
    Question question;

}
