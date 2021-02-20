package com.example.demo;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Controller
@RequestMapping("/a")
public class A {

    public A(){
        System.out.println("构造器实例化");
    }

    @PostConstruct
    private void f1(){
        System.out.println("f1(),后于构造器");
    }

    @PreDestroy
    void f2(){
        System.out.println("销毁前");
    }

    @ResponseBody//返回不是网页，仅字符串当body
    @RequestMapping("/f")
    public String f(){
        return "明翰来了";
    }
}
