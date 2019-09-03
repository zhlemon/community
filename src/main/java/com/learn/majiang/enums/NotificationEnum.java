package com.learn.majiang.enums;

public enum NotificationEnum {
    REPLY_QUESTION(1,"回复了问题"),
    REPLY_COMMENT(2,"回复了评论");

    private Integer type;
    private String name;

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationEnum(Integer status, String name) {
        this.type = status;
        this.name = name;
    }

}
