package com.cxk.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxk.usercenter.model.domain.User;
import com.cxk.usercenter.service.UserService;
import com.cxk.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cxk.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author houyunfei
 * @description 针对表【tb_user(用户)】的数据库操作Service实现
 * @createDate 2023-03-08 23:06:56
 */
@Service
@Slf4j //日志
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    private static final String SALT = "cxk";


    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验账号密码
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        if (userAccount.length() < 2) {
            return -1;
        }

        if (userPassword.length() < 2 || checkPassword.length() < 2) {
            return -1;
        }


        //账户不能包含特殊字符
        String validPattern = "[^0-9a-zA-Z_]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }

        //校验密码和确认密码是否一致
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        //账户不能重复

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(userQueryWrapper);
        if (count > 0) {
            return -1;
        }

        //密码加密：
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());


        //插入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(newPassword);
        boolean save = this.save(user);
        if (save) {
            log.info("用户注册成功，用户id为：{}", user.getUserId());
            return user.getUserId();
        } else {
            log.info("用户注册失败");
            return -1;

        }
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验账号密码
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 2) {
            return null;
        }

        if (userPassword.length() < 2) {
            return null;
        }


        //账户不能包含特殊字符
        String validPattern = "[^0-9a-zA-Z_]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }


        //密码加密：
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        userQueryWrapper.eq("userPassword", newPassword);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null) {
            log.info("用户登录失败，账号或密码错误,或者账号不存在");
        }

        //用户脱敏
        User safetyUser = getSafetyUser(user);

        //记录用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }


    /**
     * 用户脱敏
     * @param user
     * @return
     */
    public User getSafetyUser(User user) {
        User safetyUser = new User();
        safetyUser.setUserId(user.getUserId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUserRole(user.getUserRole());
        return safetyUser;
    }
}




