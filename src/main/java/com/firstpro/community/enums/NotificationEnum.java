package com.firstpro.community.enums;

public enum  NotificationEnum {
    REPLY_QUESTION(1, "replies to the question"),
    REPLY_COMMENT(2, "replies to the comment")
    ;

    private int type;
    private String name;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationEnum(int status, String name){
        this.name = name;
        this.type = status;
    }

    public static String nameOfType(int type){
        for (NotificationEnum value : NotificationEnum.values()) {
            if(value.getType() == type){
                return value.getName();
            }
        }
        return "";
    }

}
