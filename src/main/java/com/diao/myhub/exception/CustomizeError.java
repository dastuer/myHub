package com.diao.myhub.exception;

public enum CustomizeError implements ICustomizeError {
    QUESTION_NOT_FOUND(1001,"问题不存在"),
    NO_LOGIN(1002, "当前操作需要登录，请先登录"),
    SYS_ERROR(1003, "请求出错"),
    PAGE_NOT_FOUND(1004,"输入页面不存在"),
    COMMENT_NOT_FOUND(1005,"回复不存在"),
    COMMENT_TYPE_ERROR(1006,"评论类型错误"),
    COMMENT_PARAMS_ERROR(1007,"评论参数错误"),
    COMMENT_FIELD(1008,"评论失败"),
    COMMENT_CONTENT_NULL(1009,"评论内容不能为空"),
    ;

    CustomizeError(Integer code,String msg) {
        this.code = code;
        this.msg = msg;
    }
    private Integer code;
    private String msg;

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public Integer errorCode() {
        return code;
    }
}
