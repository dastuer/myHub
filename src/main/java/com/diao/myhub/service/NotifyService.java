package com.diao.myhub.service;

import com.diao.myhub.dto.NotifyDTO;
import com.diao.myhub.enums.NotifyStatusEnum;
import com.diao.myhub.enums.NotifyTypeEnum;
import com.diao.myhub.model.Comment;
import com.diao.myhub.model.Notify;

import java.util.List;

public interface NotifyService {
    Notify getNotifyById(Long id);
    int addNotify(Comment comment);
    List<NotifyDTO> getNotifyDTOS(Integer receiver);
    int readMsg(int uid);
    int readOne(Long id);
    int delNotify(Long id);
}
