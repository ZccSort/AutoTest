package com.tester.model;
import lombok.Data;
/**
 * 更新用户信息
 */
@Data
public class UpdateUserInfoCase {
    private  int id;

    private  int userId;

    private String userName;

    private  String sex;

    private  String age;

    private  String permission;

    //是否删除
    private  String isDelete;
    //预期结果
    private  String expected;





}
