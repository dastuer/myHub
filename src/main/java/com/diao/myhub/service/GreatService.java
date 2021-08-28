package com.diao.myhub.service;

import com.diao.myhub.enums.LikeStatusEnum;
import com.diao.myhub.enums.LikeTypeEnum;
import com.diao.myhub.model.Great;

import java.util.List;

public interface GreatService {
    LikeStatusEnum getIsLikeStatus(int userId, Long likeId, LikeTypeEnum typeEnum);

    List<Great> getLikeRecord(Integer id, Short type, Long likeId);

    int createRecord(Great great);

    int updateRecord(Great great);
}
