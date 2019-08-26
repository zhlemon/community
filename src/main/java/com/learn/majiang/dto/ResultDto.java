package com.learn.majiang.dto;

import com.learn.majiang.exception.CustomizeErrorCode;
import com.learn.majiang.exception.CustomizeException;
import lombok.Data;

@Data
public class ResultDto {

    private Integer code;

    private String message;

    public static ResultDto errorOf(Integer code,String message){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(code);
        resultDto.setMessage(message);
        return resultDto;
    }

    public static ResultDto errorOf(CustomizeErrorCode customizeErrorCode) {
        return errorOf(customizeErrorCode.getCode(),customizeErrorCode.getMessage());
    }

    public static ResultDto okOf(){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        return resultDto;
    }

    public static ResultDto errorOf(CustomizeException e){
        return errorOf(e.getCode(),e.getMessage());
    }

}
