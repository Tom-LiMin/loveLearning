package com.course.server.enums;

public enum CourseLevelEnum {
    ZERO("0","未定"),
    ONE("1", "初级"),
    TWO("2", "中级"),
    THREE("3", "高级");

    private String code;
    private String desc;

    CourseLevelEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
