package com.learn.majiang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {
    private String content;
    private String image;
    private String commentorName;
    private String commentTime;
    private Integer questionId;
}
