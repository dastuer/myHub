package com.diao.myhub.service;

import com.diao.myhub.dto.CommentDTO;
import com.diao.myhub.dto.MyCommentsDTO;
import com.diao.myhub.enums.CommentTypeEnum;
import com.diao.myhub.model.Comment;

import java.util.List;

public interface CommentService {
    int addComment(Comment comment);
    int getCommentCount(Long pid);
    List<MyCommentsDTO> getMyCommentDTOSByCommenterId(Integer cid);
    List<CommentDTO> getCommentsByPid(Long qid, CommentTypeEnum typeEnum);
    int delCommentById(Long id);
    Comment getCommentById(Long id);
    int getTotalSubCommentCount(List<CommentDTO> commentDTOS);
}
