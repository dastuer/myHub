package com.diao.myhub.dto;

import com.diao.myhub.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private Integer creator;
    private String description;
    private String tag;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private long gmtCreate;
    private long gmtModify;
    private User user;
}
