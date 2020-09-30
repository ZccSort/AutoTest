package com.course.httpclient.cookies;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForPost {
    private String url;

    //定义用来存储cookies信息的变量
    CookieStore cookieStore;
    private ResourceBundle resourceBundle;
    @BeforeTest
    public void beforeTest(){
        //读取配置文件
        resourceBundle = ResourceBundle.getBundle("application",Locale.CHINA);
        url=resourceBundle.getString("test.url");

    }
    /**
     * 从application配置中读出路径，拼出路径，获取cookie
     * @throws IOException
     */
    @Test
    public void testGetCookies() throws IOException {
        System.out.println();
        //从配置文件中，拼接测试的url
        String uri=resourceBundle.getString("getCookies.uri");
        System.out.println("the uri is :"+uri);
        String testUrl=this.url+uri;
        System.out.println("the complement url is :"+testUrl);
        //测试逻辑
        HttpGet httpGet=new HttpGet(testUrl);
        DefaultHttpClient httpClient=new DefaultHttpClient();
        HttpResponse response = httpClient.execute(httpGet);

        String result=EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);

        //获取cookies信息
        this.cookieStore = httpClient.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        for(Cookie cookie:cookies){
            System.out.println(cookie.getName()+"->"+cookie.getValue());
        }
    }
    @Test(dependsOnMethods = {"testGetCookies"})
    public void testPostMethod() throws IOException {
        String uri=resourceBundle.getString("test.post.with.cookie");
        String restUrl=url+uri;
        DefaultHttpClient httpClient=new DefaultHttpClient();
        //声明一个方法，这个方法是Post方法
        HttpPost httpPost=new HttpPost(restUrl);
        //添加参数
        JSONObject param=new JSONObject();
        param.put("name","zhouchao");
        param.put("age","254");
        //设置请求信息,header
        httpPost.setHeader("content-type","application/json");
        //将参数添加到方法中
        StringEntity entity =new StringEntity(param.toString(),"utf-8");
        httpPost.setEntity(entity);
        //设置cookies信息
        httpClient.setCookieStore(this.cookieStore);
        //执行post方法
        HttpResponse response=httpClient.execute(httpPost);
        //获取响应结果
        String result=EntityUtils.toString(response.getEntity());
        System.out.println(result);
        //处理结果，就是判断返回值是否符合预期
          //将返回的响应结果字符串转为json对象
          //具体判断返回结果的值
        JSONObject resultJson=new JSONObject(result);
        String status= (String) resultJson.get("status");
        String actualResult= (String) resultJson.get("zhouchao");
        Assert.assertEquals("success",actualResult);
        Assert.assertEquals("1",status);
    }
}
