package com.diao.myhub.service;

import com.diao.myhub.dto.NotifyDTO;
import com.diao.myhub.enums.CommentTypeEnum;
import com.diao.myhub.enums.NotifyStatusEnum;
import com.diao.myhub.enums.NotifyTypeEnum;
import com.diao.myhub.exception.CustomizeError;
import com.diao.myhub.exception.CustomizeException;
import com.diao.myhub.mapper.NotifyMapper;
import com.diao.myhub.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NotifyServiceImpl implements NotifyService {
    @Autowired
    public void setNotifyMapper(NotifyMapper notifyMapper) {
        this.notifyMapper = notifyMapper;
    }
    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }
    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private CommentService commentService;
    private NotifyMapper notifyMapper;
    private QuestionService questionService;
    private UserService userService;
    @Override
    public Notify getNotifyById(Long id) {
        return notifyMapper.selectByPrimaryKey(id);
    }
    @Override
    public int addNotify(Comment comment) {
        // 创建通知
        Notify notify = new Notify();
        notify.setNotifier(comment.getCommenter());
        notify.setGmtCreate(System.currentTimeMillis());
        notify.setStatus(NotifyStatusEnum.UNREAD.getStatus());
        notify.setOuterId(comment.getParentId());
        notify.setOuterTitle(comment.getContent());
        Integer receiver;
        if (comment.getType().equals(CommentTypeEnum.COMMENT_TYPE_QUESTION.getType())){
            notify.setType(NotifyTypeEnum.REPLAY_QUESTION.getType());
            Long parentId = comment.getParentId();
            receiver = questionService.getQuestion(parentId).getCreator();
        }
        else if (comment.getType().equals(CommentTypeEnum.COMMENT_TYPE_COMMENT.getType())){
            notify.setType(NotifyTypeEnum.REPLAY_COMMENT.getType());
            Long parentId = comment.getParentId();
            receiver = commentService.getCommentById(parentId).getCommenter();
        }else {
            throw new CustomizeException(CustomizeError.COMMENT_TYPE_ERROR);
        }
        // 如果被通知者和自己是同一人,则直接返回
        if (receiver.equals(notify.getNotifier())){
            return 0;
        }
        notify.setReceiver(receiver);
        userService.updateUnread(receiver);
        return notifyMapper.insertSelective(notify);
    }
    @Override
    public List<NotifyDTO> getNotifyDTOS(Integer receiver) {
        List<NotifyDTO> notifyDTOS = new ArrayList<>();
        NotifyExample notifyExample = new NotifyExample();
        notifyExample.createCriteria().andReceiverEqualTo(receiver);
        notifyExample.setOrderByClause("gmt_create desc");
        List<Notify> notifies = notifyMapper.selectByExample(notifyExample);
        for (Notify notify : notifies) {
            NotifyDTO notifyDTO = new NotifyDTO();
            Integer notifier = notify.getNotifier();
            User userById = userService.getUserById(notifier);
            notifyDTO.setUser(userById);
            if (notify.getType().equals(NotifyTypeEnum.REPLAY_QUESTION.getType())){
                Question question = questionService.getQuestion(notify.getOuterId());
                notifyDTO.setQuestion(question);

            }
            if (notify.getType().equals(NotifyTypeEnum.REPLAY_COMMENT.getType())){
                Comment commentById = commentService.getCommentById(notify.getOuterId());
                Question question = questionService.getQuestion(commentById.getParentId());
                notifyDTO.setQuestion(question);
                notifyDTO.setComment(commentById);
            }
            BeanUtils.copyProperties(notify,notifyDTO);
            notifyDTOS.add(notifyDTO);
        }
        return notifyDTOS;

    }
    @Override
    public int readMsg(int uid) {
       return notifyMapper.readMsg(uid,NotifyStatusEnum.READ.getStatus());
    }
    @Override
    public int readOne(Long id) {
        return notifyMapper.readOneMsg(id,NotifyStatusEnum.READ.getStatus());
    }
    @Override
    public int delNotify(Long id) {
        return notifyMapper.deleteByPrimaryKey(id);
    }
}
