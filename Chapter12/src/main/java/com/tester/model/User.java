package com.tester.model;

import lombok.Data;

/**
 * 用户信息
 */
@Data
public class User {

    private  int id;

    private  String userName;

    private  String password;
    private String  age;

    private String sex;

    private String permission;
    //是否删除
    private String isDelete;

    @Override
    public String toString(){
         return (
                 "{id:"+id+","+
                 "userName:"+userName+","+
                 "password:"+password+","+
                 "age:"+age+","+
                 "sex:"+sex+","+
                 "permission:"+permission+","+
                 "isDelete:"+isDelete+"}"
                 );
    }

}
