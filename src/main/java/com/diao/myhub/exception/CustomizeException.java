package com.diao.myhub.exception;

public class CustomizeException extends RuntimeException{
    private String message = "请求出错";
    private Integer code= 1000;
    public CustomizeException(ICustomizeError error) {
        this.message= error.getMsg();
        this.code = error.errorCode();
    }
    public CustomizeException() {
    }
    @Override
    public String getMessage() {
        return message;
    }
    public Integer getCode() {
        return code;
    }
}
