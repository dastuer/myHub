package com.diao.myhub.enums;

public enum NotifyStatusEnum {
    READ((short) 1),
    UNREAD((short) 0),
    ;
    private final short status;

    NotifyStatusEnum(short status) {
        this.status = status;
    }

    public short getStatus() {
        return status;
    }
}
