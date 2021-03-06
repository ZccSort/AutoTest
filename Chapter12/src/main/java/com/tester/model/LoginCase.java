package com.tester.model;

import lombok.Data;

/**
 * 用户登录接口的测试用例
 */
@Data
public class LoginCase {
    private  int id;

    private  String userName;

    private  String password;
    //预期结果
    private  String expected;
}
