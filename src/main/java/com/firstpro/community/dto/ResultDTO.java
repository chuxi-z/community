package com.firstpro.community.dto;

import com.firstpro.community.exception.CustomizeErrorCode;
import com.firstpro.community.exception.CustomizeException;
import lombok.Data;
import org.springframework.web.servlet.ModelAndView;

@Data
public class ResultDTO {
    private Integer code;
    private String message;


    public static ResultDTO errorOf(Integer code, String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setMessage(message);
        resultDTO.setCode(code);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeErrorCode noLogin) {
        return errorOf(noLogin.getCode(), noLogin.getMessage());
    }

    public static ResultDTO passOf(){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setMessage("Request is allowed!!!");
        resultDTO.setCode(200);
        return resultDTO;
    }


    public static ResultDTO errorOf(CustomizeException ex) {
        return errorOf(ex.getCode(), ex.getMessage());
    }
}
