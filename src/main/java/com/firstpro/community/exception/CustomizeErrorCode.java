package com.firstpro.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"The question you are looking for does not exist..."),
    TARGET_PARAM_NOT_FOUND(2002,"No question or comment is selected..."),
    NO_LOGIN(2003 ,"Need login first..."),
    SYS_ERROR(2004 ,"Server exception..."),
    TYPE_PARAM_WRONG(2005 ,"Type of comment is wrong or does not exist..."),
    COMMENT_NOT_FOUND(2006, "The comment does not exist...");

    @Override
    public String getMessage(){
        return message;
    }

    @Override
    public Integer getCode(){
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
