package com.cxk.usercenter.common;

public class RUtils {

    public static <T> R<T> success(T data) {
        return new R<>(0, data, "ok");
    }
}
