package com.cxk.usercenter.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxk.usercenter.common.ErrorCode;
import com.cxk.usercenter.common.R;
import com.cxk.usercenter.common.RUtils;
import com.cxk.usercenter.exception.BusinessException;
import com.cxk.usercenter.model.domain.User;
import com.cxk.usercenter.model.request.UserRegisterRequest;
import com.cxk.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.cxk.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.cxk.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author houyunfei
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public R<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)){
            return RUtils.error(ErrorCode.PARAM_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return RUtils.success(result);
    }

    @PostMapping("/login")
    public R<User> userLogin(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        if (user != null) {
            log.info("登录成功");
            log.info("登录的用户信息为：{}", request.getSession().getAttribute(USER_LOGIN_STATE));
        }
        return RUtils.success(user);
    }

    @PostMapping("/logout")
    public R<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        Integer result = userService.userLogout(request);
        return RUtils.success(result);
    }

    @GetMapping("/current")
    public R<User> getCurrentUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            log.warn("获取当前用户信息：用户未登录");
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        long userId = user.getUserId();
        //todo:校验用户是否合法
        User newUser = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(newUser);
        return RUtils.success(safetyUser);
    }

    @GetMapping("/search")
    public R<List<User>> searchAllUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            log.info("没有权限");
            return null;
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            userQueryWrapper.like("username", username);
        }
        List<User> userList = userService.list(userQueryWrapper);
        List<User> users = userList.stream().map(user -> {
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
        return RUtils.success(users);

    }

    @PostMapping("/delete")
    public R<Boolean> deleteUserById(@RequestBody Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return null;
        }
        if (id <= 0) {
            log.info("用户的ID不合法");
            return null;
        }
        boolean result = userService.removeById(id);
        return RUtils.success(result);
    }


    /**
     * 判断是否是管理员
     *
     * @param request 请求
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            log.info("没有登录");
            return false;
        }
        log.info("用户已登录，权限已查询，权限状态为：" + (user.getUserRole() == ADMIN_ROLE ? "管理员" : "普通用户"));
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }


}
