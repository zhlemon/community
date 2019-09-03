package com.learn.majiang.dto;

import com.learn.majiang.model.User;
import lombok.Data;

@Data
public class NotificationDto {
    private Integer id;
    private Long gmtCreate;
    private Integer status;
    private User notifier;
    private String outfile;
    private String type;
}
