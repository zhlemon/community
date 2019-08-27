package com.learn.majiang.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"你找的问题不在了，要不换个试试?"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    USER_HAS_NOT_LOGIN_IN(2003,"用户未登录，请登录后重试。"),
    SYS_ERROR(2004,"服务器冒烟了，稍后再试试吧。。。"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你找的评论不存在了，要不换个试试？"),
    ;


    private String message;

    private Integer code;

    CustomizeErrorCode(Integer code,String message) {
        this.code=code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }


}
