package com.zhouchao.chapter10.server;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import sun.awt.SunHints;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@Api(value = "/",description = "这是我全部的get方法")
public class MyGetMethod {

    @RequestMapping(value = "/getCookies",method = RequestMethod.GET)
    @ApiOperation(value = "通过这个方法可以获取cookie值",httpMethod ="GET")
    public String getCookies(HttpServletResponse response){
        Cookie cookie=new Cookie("login","value");
        response.addCookie(cookie);
        return "恭喜你获得cookies信息成功";
    }
    /**
     * 要求客户端携带cookie访问
     */
    @RequestMapping(value = "/get/with/cookies",method = RequestMethod.GET)
    @ApiOperation(value = "要求客户端携带cookie访问",httpMethod = "GET")
     public String getWithCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(Objects.isNull(cookies)){
            return "必须携带cookies信息";
        }
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("login")&&cookie.getValue().equals("true")){
                return "访问成功";
            }
        }
        return "必须携带cookies信息";
     }
    /**
     * 开发一个需要携带参数才能访问的get请求
     * 第一种实现方式,url:key=value&key=value
     */
    @RequestMapping(value = "/get/with/param",method = RequestMethod.GET)
    @ApiOperation(value = "需要携带参数才能访问的get请求方法的第一种实现",httpMethod = "GET")
    public Map<String,Integer> getList(@RequestParam Integer start,
                                       @RequestParam Integer end){
        Map<String,Integer> mapList=new HashMap<String,Integer>();
        mapList.put("鞋",400);
        mapList.put("干脆面",1);
        mapList.put("衬衫",300);
        return mapList;
    }
    /**
     * 第二种需要携带参数访问的get请求
     * url:ip:port/get/with/param/10/20
     */
    @RequestMapping(value = "/get/with/param/{start}/{end}")
    @ApiOperation(value = "需要携带参数才能访问的get方法的第二种实现",httpMethod = "GET")
    public Map<String,Integer> myGetList(@PathVariable Integer start,@PathVariable Integer end){
        Map<String,Integer> mapList=new HashMap<String,Integer>();
        mapList.put("鞋",8955600);
        mapList.put("干脆面",1);
        mapList.put("衬衫",300);
        System.out.println();
        return mapList;
    }
}
