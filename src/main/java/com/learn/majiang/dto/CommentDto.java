package com.learn.majiang.dto;

import com.learn.majiang.model.User;
import lombok.Data;

@Data
public class CommentDto {
    private Integer id;
    private Integer parentid;
    private Integer type;
    private String commentor;
    private Long gmtcreate;
    private Long gmtmodified;
    private Long likecount;
    private String content;
    private User user;
}
