package com.nowcoder.community.d_controller;


import com.nowcoder.community.z_util.CommunityUtil;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.HttpCookie;

//测试cookie，session
@Controller
public class TestController {

    @ResponseBody
    @RequestMapping(path = "/ck",method = RequestMethod.GET)
    public String setCookie(HttpServletResponse response){
        //创cookie
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());//死的给uid【直接粘】
        //cookie.setPath("/community"); // 设置cookie生效的范围【错在这，改成全局就好了】
        cookie.setMaxAge(60 * 10);// 设置cookie的生存时间
        //【核】response带回ck给浏览器
        response.addCookie(cookie);

        return "suc";//没用
    }

    //之后的其他方法自带ck
    @ResponseBody
    @RequestMapping(path = "/zidai",method = RequestMethod.GET)
    public String getCookie(@CookieValue("code")String code) {
        System.out.println(code);
        return "get cookie";
    }


    //设置session
    //tomcat自动有session不用创
    @ResponseBody
    @RequestMapping(path = "/se",method = RequestMethod.GET)
    private String setSession(HttpSession session){
        session.setAttribute("name","liying");
        session.setAttribute("age","25");
        return "sesion respon自动塞入，不用new不用add。之后的所有访问都带这个session，因为我设的全局";
    }
    @ResponseBody
    @RequestMapping(path = "/kankzt",method = RequestMethod.GET)
    private String getSession(HttpSession session){
        //服务器端sout测试
        System.out.println(session.getAttribute("name"));
        System.out.println(session.getAttribute("age"));
        return "看控制台";
    }
}
