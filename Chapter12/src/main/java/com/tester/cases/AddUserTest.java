package com.tester.cases;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.tester.config.TestConfig;
import com.tester.model.AddUserCase;
import com.tester.model.User;
import com.tester.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.xml.transform.Result;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class AddUserTest {
    @Test(dependsOnGroups = "loginTrue",description = "添加用户测试")
    public void addUser() throws IOException, InterruptedException {
        SqlSession session=DatabaseUtil.getSqlSession();
        //第一次从数据库中查询数据，1.为什么要从数据库中查，因为设计的接口测试用例是存在数据中的
        //2.就是从数据库中查询到测试用例，然后直接发送请求中带着查询的数据返回结果，实现接口自动化
        AddUserCase addUserCase=session.selectOne("addUserCase",2);
        System.out.println(addUserCase.toString());

        //发送请求
         String result=getResult(addUserCase);
        //这里需要进行休眠,原因是你还没请求，就开始查询，肯定会出错
        Thread.sleep(5000);
        //验证返回结果

        User user=session.selectOne("addUser",addUserCase);
        Thread.sleep(5000);
        System.out.println(user.toString());
        Assert.assertEquals(addUserCase.getExpected(),result);
    }
    //因为接口的返回值是"true/fasle"
    private String getResult(AddUserCase addUserCase) throws IOException {
        //拿到添加用户接口的url
        HttpPost httpPost=new HttpPost(TestConfig.addUserUrl);
        JSONObject param=new JSONObject();
        param.put("userName",addUserCase.getUserName());
        param.put("password",addUserCase.getPassword());
        param.put("sex",addUserCase.getSex());
        param.put("age",addUserCase.getAge());
        param.put("permission",addUserCase.getPermission());
        param.put("isDelete",addUserCase.getIsDelete());
        //设置头信息
        httpPost.setHeader("content-type","application/json");
        StringEntity entity =new StringEntity(param.toString(),"utf-8");
        httpPost.setEntity(entity);

        //设置cookies
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.cookieStore);
        //这里的返回结果是真正接口处UserManager中的addUser接口的返回值，是boolean类型的
        HttpResponse response=TestConfig.defaultHttpClient.execute(httpPost);
        String result=EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        return result;
    }
}
