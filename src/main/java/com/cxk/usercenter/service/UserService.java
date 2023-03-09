package com.cxk.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cxk.usercenter.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author houyunfei
 * @description 针对表【tb_user(用户)】的数据库操作Service
 * @createDate 2023-03-08 23:06:56
 */
public interface UserService extends IService<User> {



    /**
     * 用户注册
     *
     * @param userAccount   账号
     * @param userPassword  密码
     * @param checkPassword 确认密码
     * @return 1-成功  -1-失败
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);


    /**
     * 用户登录
     *
     * @param userAccount  账号
     * @param userPassword 密码
     * @param request      请求
     * @return 用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param user
     * @return
     */
    User getSafetyUser(User user);

}
