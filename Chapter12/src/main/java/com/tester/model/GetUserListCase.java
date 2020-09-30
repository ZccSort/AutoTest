package com.tester.model;

import lombok.Data;

/**
 * 获取用户列表
 */
@Data
public class GetUserListCase {
    private  String userName;

    private String age;

    private String sex;

    private  String expected;

}
