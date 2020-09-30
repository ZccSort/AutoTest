package com.course.httpclient.cookies;

import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MyCookiesForGet {
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

    /**
     * 通过携带cookie来做请求访问
     * @throws IOException
     */
     @Test(dependsOnMethods ={"testGetCookies"} )
     public void testGetWithCookies() throws IOException {
          String uri=resourceBundle.getString("test.get.with.cookies");
          String testUrl=url+uri;

          HttpGet httpGet=new HttpGet(testUrl);
          DefaultHttpClient httpClient=new DefaultHttpClient();

          //设置cookies信息
         httpClient.setCookieStore(this.cookieStore);
         //执行
         HttpResponse response=httpClient.execute(httpGet);
         //获取响应的状态码
         int statusCode=response.getStatusLine().getStatusCode();
         System.out.println("the status is :"+statusCode);
         //获取响应的值
         if(statusCode==200) {
             String responseValue = EntityUtils.toString(response.getEntity(), "utf-8");
             System.out.println(responseValue);
         }
     }
}
