package com.firstpro.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND("The question you are looking for does not exist...");

    @Override
    public String getMessage(){
        return message;
    }

    private String message;

    CustomizeErrorCode(String message){
        this.message = message;
    }
}
