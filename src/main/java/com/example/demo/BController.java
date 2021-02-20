package com.example.demo;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller //测试请求，响应
public class BController {


    @RequestMapping("http")
    public void f(HttpServletRequest request, HttpServletResponse response){
        //测试/r配置-连接
        System.out.println(request);System.out.println(response);

        //测试能否返回
        response.setContentType("text/html;charset=utf-8");//告诉是直接返回值 不用@Rbody
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write("<h1>测试服务器响应成功！</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.close();//不关会卡死
        }

    }

    //测试显式拿参数//在浏览器上手敲：http://localhost:8080/r/t1?a=1&b=2
    @ResponseBody
    @RequestMapping(path="/t1",method = RequestMethod.GET)
    public String f(int a,int b){//所有函数必须String返回，除非用(req,rep)可以void
        System.out.println(a+","+b);
        return a+b+"";
    }

    //测试用表单隐式拿
    @ResponseBody
    @RequestMapping(path = "/biaodan")//前连/r
    public String f1(String name ,int age){//必须同名，要不前面要加注解声明
        System.out.println(name+","+age);
        return name+age+"岁";//不加返回，会报错显示时间
    }

    //不返回String，用MV（springMVC智能自己识别！）
    @RequestMapping("/mv")
    public ModelAndView f2(){
        //生成model
        ModelAndView mv = new ModelAndView();
        mv.addObject("where","haerbin");
        mv.addObject("school","哈工程");
        //生成view
        mv.setViewName("/m1");
        return mv;
    }

    //测试java类型返回网页（自动转json格式了）
    @ResponseBody
    @RequestMapping(path = "/jsonT")
    public List<Map<Integer,String>> f(){
        //必须先定义HM，再定义List<包>
        Map<Integer,String> map = new HashMap<>(); map.put(1,"李赢");map.put(2,"秦城");map.put(3,"开锣");
        Map<Integer,String> map1 = new HashMap<>(); map1.put(1,"李赢1");map1.put(2,"秦城1");map1.put(3,"开锣1");
        List<Map<Integer,String>> list = new ArrayList<>();
        list.add(map);
        list.add(map1);

        return list;
    }
}
