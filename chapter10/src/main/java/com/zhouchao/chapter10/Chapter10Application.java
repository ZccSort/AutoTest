package com.zhouchao.chapter10;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@EnableAutoConfiguration
@Controller
@ComponentScan("com.zhouchao.chapter10")
public class Chapter10Application {

    @ResponseBody
    @RequestMapping("/method")
    public String  method(){
        return "hello world";
    }
    public static void main(String[] args) {
        SpringApplication.run(Chapter10Application.class, args);
    }

}
