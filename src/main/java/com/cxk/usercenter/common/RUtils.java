package com.cxk.usercenter.common;

public class RUtils {

    public static <T> R<T> success(T data) {
        return new R<>(0, data, "ok");
    }


    public static R error(ErrorCode errorCode) {
        return new R(errorCode);
    }

    public static R error(ErrorCode errorCode, String description) {
        return new R(errorCode.getCode(), null, errorCode.getMessage(), description);
    }

    public static R error(ErrorCode errorCode, String message, String description) {
        return new R(errorCode.getCode(), null, message, description);
    }

    public static R error(int code, String message, String description) {
        return new R(code, null, message, description);
    }


}
