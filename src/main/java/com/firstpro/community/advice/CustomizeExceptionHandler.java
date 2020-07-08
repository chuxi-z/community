package com.firstpro.community.advice;

import com.alibaba.fastjson.JSON;
import com.firstpro.community.dto.ResultDTO;
import com.firstpro.community.exception.CustomizeErrorCode;
import com.firstpro.community.exception.CustomizeException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    Object handler(HttpServletRequest request, Throwable ex, Model model, HttpServletResponse response){
        HttpStatus status = getStatus(request);

        String contentType = request.getContentType();
        if("application/json".equals(contentType)){
            //返回json
            ResultDTO resultDTO = null;
            if(ex instanceof CustomizeException){
                resultDTO = ResultDTO.errorOf((CustomizeException) ex);
            }
            else{
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            PrintWriter writer = null;
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException e) {
            }
        }
        else{
            //错误页面跳转
            if(ex instanceof CustomizeException){
                model.addAttribute("message", ex.getMessage());
            }
            else{
                model.addAttribute(CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
        return null;
    }

    private HttpStatus getStatus(HttpServletRequest request){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == null){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
