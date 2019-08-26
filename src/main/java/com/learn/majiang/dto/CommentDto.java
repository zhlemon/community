package com.learn.majiang.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Integer parentId;
    private String content;
    //类型 表示这是问题还是评论
    private Integer type;
}
