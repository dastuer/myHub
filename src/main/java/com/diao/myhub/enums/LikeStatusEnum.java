package com.diao.myhub.enums;

public enum LikeStatusEnum {
    LIKE_STATUS_LIKED((short) 1),
    LIKE_STATUS_UNLIKE((short) 0);

    LikeStatusEnum(short status) {
        this.status = status;
    }

    public short getStatus() {
        return status;
    }

    private short status;
}
