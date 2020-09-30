package com.tester.model;

import lombok.Data;

/**
 * 添加用户信息
 */
@Data
public class AddUserCase {
    private  int id;
    private  String  userName;

    private  String password;

    private String sex;

    private  String age;

    private String permission;

    //是否删除
    private  String isDelete;
    //期望结果
    private String expected;

}
