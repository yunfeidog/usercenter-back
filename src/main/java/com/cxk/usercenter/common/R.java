package com.cxk.usercenter.common;

import lombok.Data;

import java.io.Serializable;


/**
 * 通用返回对象
 * @param <T> 返回的数据类型
 */
@Data
public class R<T> implements Serializable {
    private int code;

    private T data;

    private String message;

    public R(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public R(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
