package com.diao.myhub.dto;

import com.diao.myhub.model.User;
import lombok.Data;

/**
 * @author Morty
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer commenter;
    private Short type;
    private Long gmtCreate;
    private Long gmtModify;
    private Long likeCount;
    private Short likedStatus;
    private String content;
    private User user;
    private Long subCommentCount;
}
