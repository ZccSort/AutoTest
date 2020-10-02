package com.tester.cases;

import com.tester.config.TestConfig;
import com.tester.model.GetUserInfoCase;
import com.tester.model.User;
import com.tester.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetUserInfoTest {
    @Test(dependsOnGroups = "loginTrue",description = "获取userId为1的用户信息")
    public void getUserInfo() throws IOException, InterruptedException {
        SqlSession session=DatabaseUtil.getSqlSession();
        GetUserInfoCase getUserInfoCase=session.selectOne("getUserInfoCase",1);
        System.out.println(getUserInfoCase.toString());
        System.out.println(TestConfig.getUserInfoUrl);

        JSONArray resultJson=getJsonResult(getUserInfoCase);
        //这里需要进行休眠,原因是你还没请求，就开始查询，肯定会出错
        Thread.sleep(3000);
        //获取更新都需要将mybatis的xml中的id存储到数据库中吗？
        //难道是区分返回true,和返回对象或者对象的集合？1
        //这里其实和User user=session.selectOne("addUser",addUserCase);一样，只不过将第一个参数存储在数据库中而已
        User user=session.selectOne(getUserInfoCase.getExpected(),getUserInfoCase);
        List<User> userList=new ArrayList<User>();
        userList.add(user);
        JSONArray jsonArray=new JSONArray(userList);
        JSONArray jsonArray2=new JSONArray(resultJson.getString(0));
        //比较发请求后的数据和直接从数据库中出来的数据进行比较
        //要先转成字符串再比较
        Assert.assertEquals(jsonArray2.toString(),jsonArray.toString());
    }
    //原接口的返回值为List<User>
    private JSONArray getJsonResult(GetUserInfoCase getUserInfoCase) throws IOException {
        HttpPost httpPost=new HttpPost(TestConfig.getUserInfoUrl);
        JSONObject param=new JSONObject();
        param.put("id",getUserInfoCase.getUserId());
        httpPost.setHeader("content-type","application/json");

        StringEntity entity=new StringEntity(param.toString(),"utf-8");
        httpPost.setEntity(entity);
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.cookieStore);
        HttpResponse response=TestConfig.defaultHttpClient.execute(httpPost);
        String result=EntityUtils.toString(response.getEntity(),"utf-8");
        List resultList=Arrays.asList(result);
        //先把字符串转为List，然后再转为JSONArray
        JSONArray array=new JSONArray(resultList);
        return array;
    }
}
