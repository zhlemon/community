package com.learn.majiang.dto;

import com.learn.majiang.model.User;
import lombok.Data;

@Data
public class QuestionDto {
    private Integer id;
    private String title;
    //问题描述
    private String description;
    private Long gmtcreate;
    private Long gmtmodified;
    //问题创建者
    private Integer creator;
    private Integer commentcount;
    private Integer viewcount;
    private Integer likecount;
    private String tag;
    private User user;
}
