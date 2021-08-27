package com.diao.myhub.enums;

public enum CommentTypeEnum {
    COMMENT_TYPE_QUESTION((short)1),
    COMMENT_TYPE_COMMENT((short)2);
    private Short type;
    CommentTypeEnum(Short type){
        this.type = type;
    }
    public Short getType(){
        return type;
    }
}
