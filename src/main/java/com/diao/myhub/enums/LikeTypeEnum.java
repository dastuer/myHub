package com.diao.myhub.enums;

public enum LikeTypeEnum {
    LIKE_TYPE_QUESTION((short) 1),
    LIKE_TYPE_COMMENT((short) 2);
    private short type;
    LikeTypeEnum(short type) {
        this.type = type;
    }
    public short getType() {
        return type;
    }
}
