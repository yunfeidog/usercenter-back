package com.cxk.usercenter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class UserServiceTest {


    @Resource
    private UserService userService;

    @Test
     void testAddUser(){

        User user=new User();
        user.setUsername("cxk");
        user.setUserAccount("123");
        user.setAvatarUrl("https://www.baidu.com/s?wd=%E4%B8%A4%E4%BC%9A&sa=ire_dl_gh_logo_texing&rsv_dl=igh_logo_pc");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("123");
        user.setEmail("123@qq.com");
        boolean result = userService.save(user);
        System.out.println(user.getUserId());
        Assertions.assertTrue(result);
    }

    @Test
    void testUserRegister() {
        String userAccount = "cxk";
        String userPassword = "";
        String checkPassword = "123";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userPassword="123";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(1, result);


    }
}