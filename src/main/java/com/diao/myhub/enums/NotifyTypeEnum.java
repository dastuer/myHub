package com.diao.myhub.enums;

public enum NotifyTypeEnum {

    REPLAY_QUESTION((short) 1,"回复了问题"),
    REPLAY_COMMENT((short) 2,"回复了方法"),
    ;

    private final short type;
    private final String name;

    NotifyTypeEnum(short type, String name) {
        this.type = type;
        this.name = name;
    }

    public short getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
