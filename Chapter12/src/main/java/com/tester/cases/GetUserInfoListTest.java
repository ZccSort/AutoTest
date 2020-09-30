package com.tester.cases;

import com.tester.config.TestConfig;
import com.tester.model.GetUserListCase;
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
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class GetUserInfoListTest {
    @Test(dependsOnGroups = "loginTrue",description = "获取性别为男的用户信息")
    public void getUserListInfo() throws IOException {
        SqlSession session=DatabaseUtil.getSqlSession();
        GetUserListCase getUserListCase=session.selectOne("getUserListCase",1);
        System.out.println(getUserListCase.toString());
        System.out.println(TestConfig.getUserListUrl);

        //发起请求,获取结果
        JSONArray resultJson=getJsonResult(getUserListCase);

        //验证
        List<User> userList=session.selectList(getUserListCase.getExpected(),getUserListCase);
        for(User user:userList){
            System.out.println("获取的user:"+user.toString());
        }
        JSONArray userListJson=new JSONArray(userList);
        //先判断长度是否一致，第一个参数为期望结果，第二个参数为实际结果
        Assert.assertEquals(userListJson.length(),resultJson.length());
        //再判断对象是否一致
        for(int i=0;i<resultJson.length();i++){
            JSONObject expect= (JSONObject) resultJson.get(i);
            JSONObject actual= (JSONObject) userListJson.get(i);
            Assert.assertEquals(expect.toString(),actual.toString());
        }
    }

    private JSONArray getJsonResult(GetUserListCase getUserListCase) throws IOException {
        HttpPost post=new HttpPost(TestConfig.getUserListUrl);
        JSONObject param=new JSONObject();
        param.put("userName",getUserListCase.getUserName());
        param.put("sex",getUserListCase.getSex());
        param.put("age",getUserListCase.getAge());

        post.setHeader("content-type","application/json");
        StringEntity entity=new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);

        TestConfig.defaultHttpClient.setCookieStore(TestConfig.cookieStore);
        HttpResponse response=TestConfig.defaultHttpClient.execute(post);
        String result=EntityUtils.toString(response.getEntity(),"utf-8");

        JSONArray jsonArray=new JSONArray(result);


        return jsonArray;
    }
}
