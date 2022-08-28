package com.course.server.exception;

public enum BusinessExceptionCode {
    USER_LOGIN_NAME_EXIST("登录名已存在"),
    PASSWORD__RESET_FAIL("密码重复出现错误，已退出"),
    LOGIN_ERROR("用户名或密码不存在"),
    ;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
