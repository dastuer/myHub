package com.diao.myhub.model;

import lombok.Data;
import org.springframework.stereotype.Repository;

@Data
public class Question {
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
}
