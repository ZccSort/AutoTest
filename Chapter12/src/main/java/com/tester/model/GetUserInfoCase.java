package com.tester.model;

import lombok.Data;

/**
 * 获取用户信息
 */
@Data
public class GetUserInfoCase {
    private  int userId;

    //期望结果
    private  String expected;


}
