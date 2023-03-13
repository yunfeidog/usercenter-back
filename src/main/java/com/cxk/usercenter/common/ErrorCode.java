package com.cxk.usercenter.common;

/**
 * 错误码
 * @author houyunfei
 */

public enum ErrorCode {


    SUCCESS(0, "ok",""),

    PARAM_ERROR(40000, "请求参数错误",""),
    NULL_ERROR(40001, "请求参数为空",""),
    NO_LOGIN(40100, "用户未登录",""),
    NO_AUTHOR(40101, "没有权限",""),

    SYSTEM_ERROR(50000, "系统内部异常",""),

    ;
    private final int code;

    private final String message;

    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
