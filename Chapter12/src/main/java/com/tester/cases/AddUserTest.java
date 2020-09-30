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
    public void addUser() throws IOException {
        SqlSession session=DatabaseUtil.getSqlSession();
        AddUserCase addUserCase=session.selectOne("addUserCase",1);
        System.out.println(addUserCase.toString());
        System.out.println(TestConfig.addUserUrl);

        //发送请求
         String result=getResult(addUserCase);
        //接受请求，验证结果
        User user=session.selectOne("addUser",addUserCase);
        System.out.println(user.toString());
        Assert.assertEquals(addUserCase.getExpected(),result);
    }

    private String getResult(AddUserCase addUserCase) throws IOException {
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
        HttpResponse response=TestConfig.defaultHttpClient.execute(httpPost);
        String result=EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        return result;
    }
}
