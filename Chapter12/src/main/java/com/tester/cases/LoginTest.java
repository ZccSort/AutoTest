package com.tester.cases;

import com.tester.config.TestConfig;
import com.tester.model.InterfaceName;
import com.tester.model.LoginCase;
import com.tester.utils.ConfigFile;
import com.tester.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTest {

    @BeforeTest(groups = "loginTrue",description ="测试准备工作，获取httpClient" )
    public void beforeTest(){
        TestConfig.getUserInfoUrl= ConfigFile.getUrl(InterfaceName.GETUSERINFO);

        TestConfig.getUserListUrl=ConfigFile.getUrl(InterfaceName.GETUSERLIST);

        TestConfig.addUserUrl=ConfigFile.getUrl(InterfaceName.ADDUSERINFO);

        TestConfig.loginUrl=ConfigFile.getUrl(InterfaceName.LOGIN);

        TestConfig.updateUserInfoUrl=ConfigFile.getUrl(InterfaceName.UPDATEUSERINFO);

        TestConfig.defaultHttpClient=new DefaultHttpClient();
    }
    @Test(groups = "loginTrue",description = "用户登录成功接口",priority = 0)
    public void loginTrue() throws IOException {
        SqlSession session=DatabaseUtil.getSqlSession();
        LoginCase loginCase=session.selectOne("loginCase",1);
        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);

        //1.发送请求
        String result=getResult(loginCase);
        //2.响应请求，验证结果
        Assert.assertEquals(loginCase.getExpected(),result);
    }

    private String getResult(LoginCase loginCase) throws IOException {
        HttpPost httpPost=new HttpPost(TestConfig.loginUrl);
        JSONObject param=new JSONObject();
        param.put("userName",loginCase.getUserName());
        param.put("password",loginCase.getPassword());
        httpPost.setHeader("content-type","application/json");
        StringEntity entity=new StringEntity(param.toString(),"utf-8");
        httpPost.setEntity(entity);
        HttpResponse response=TestConfig.defaultHttpClient.execute(httpPost);
        String result=EntityUtils.toString(response.getEntity(),"utf-8");
        //不写这个，后面的方法无法运行的
        TestConfig.cookieStore=TestConfig.defaultHttpClient.getCookieStore();
        return result;
    }

    @Test(groups = "loginFalse",description = "用户登录失败接口",priority = 1)
    public  void loginFalse() throws IOException {
        SqlSession session=DatabaseUtil.getSqlSession();
        LoginCase loginCase=session.selectOne("loginCase",2);
        System.out.println(loginCase.toString());
        System.out.println(TestConfig.loginUrl);

        //1.发送请求
        String result=getResult(loginCase);
        //2.响应请求，验证结果
        Assert.assertEquals(loginCase.getExpected(),result);
    }
}
