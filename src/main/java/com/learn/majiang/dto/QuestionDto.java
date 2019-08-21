package com.learn.majiang.dto;

import com.learn.majiang.model.User;
import lombok.Data;

@Data
public class QuestionDto {
    private Integer id;
    private String title;
    //问题描述
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    //问题创建者
    private Integer creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
    private User user;
}
