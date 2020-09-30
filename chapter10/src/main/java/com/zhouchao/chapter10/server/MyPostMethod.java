package com.zhouchao.chapter10.server;

import com.zhouchao.chapter10.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(value = "/",description = "这是全部的post请求")
@RequestMapping(value = "/v1")
public class MyPostMethod {

    public static Cookie cookie;

    //用户登录成功获取到cookie,然后再访问其他接口获取到列表
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiOperation(value = "登录接口成功后获取cookies信息",httpMethod = "POST")
    public String login(HttpServletResponse response,
                        @RequestParam String username,
                        @RequestParam String password){
      if(username.equals("zhouchao")&&password.equals("123456")){
          cookie=new Cookie("login","true");
          response.addCookie(cookie);
          return "恭喜登录成功";
      }
      return "用户名或者密码错误";
    }
    @RequestMapping(value = "/getUserList",method = RequestMethod.POST)
    @ApiOperation(value = "获取用户列表",httpMethod = "POST")
    public String getUserList(HttpServletRequest request,
                            @RequestBody User user){
       //获取cookies
        Cookie[] cookies = request.getCookies();
        User user1=null;
        //验证cookie是否和方法
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("login")
                    &&cookie.getValue().equals("true")
                    &&user.getUsername().equals("zhouchao")
                    &&user.getPassword().equals("123456")){
                user1=new User();
               user1.setName("lisi");
               user1.setAge("24");
               user1.setSex("man");
               return user1.toString();
            }
        }
        return "参数不合法";
    }
}
