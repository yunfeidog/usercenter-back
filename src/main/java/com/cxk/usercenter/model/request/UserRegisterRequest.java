package com.cxk.usercenter.model.request;

import lombok.Data;

/**
 * @author houyunfei
 * @description 用户请求对象
 */
@Data
public class UserRegisterRequest  {

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
