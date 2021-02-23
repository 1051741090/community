package com.nowcoder.community.d_controller;

import com.nowcoder.community.a_entity.DiscussPost;
import com.nowcoder.community.a_entity.Page;
import com.nowcoder.community.a_entity.User;
import com.nowcoder.community.c_service.DiscussPostService;
import com.nowcoder.community.c_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
//开始跳转到模板分支
public class HomeController11 {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;

    //首页方法响应口 + 分页按钮的响应口
    @RequestMapping("/index")//【1这是url浏览器敲得路径】
    public String f(Model model, Page page){//【b1】【回】非page的问号后参数直接方法参数里拿，page直接回自动扫set改所有of lim cur=? //【去】工具类直接给到Controller，参数直接传过去！
        //初page2个无默认值，共聚方法需要的成员变量
        /*
        // page是参数传的，在thymeleaf中直接page。 th:text="    ${page}"
           map是java写的要                       th:each="map:${res}"，方便迭代，
        */
        page.setRows(discussPostService.findDiscussPostRows(0));//这里要帖子总数，不是用户总数.0默认查所有
        page.setPath("/index");

        //用户101的0-10题解*
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());//【？后参数自动set拿】
        //String标识+map结构体[1][2]存才能给给html的th用
        List<Map<String,Object>> discussPosts = new ArrayList<>();//map泛型爆红没事，后面只要new了HM就没了
        for(DiscussPost d:list){
            Map<String,Object> map = new HashMap<>();
            //当前用户讨论帖表行* + userid对应的用户表的信息
            map.put("post",d);
            map.put("user",userService.findUserByid(d.getUserId()));
            discussPosts.add(map);
        }
        model.addAttribute("discussPosts",discussPosts);
        return "moban/index";//自动在temp.moban下找xx.html【1这是model传给static/路径下的/index.html】
    }

    //独立的logout[（与之前无任何关系）提取cok，改1]
    @RequestMapping("/logout")
    public String genjuCkTuichu(Model model, @CookieValue("ticket") String ticket){//ck在参数拿
        userService.logout(ticket);//涉及mapper

        return "redirect:/login";//重新访问登录页，并先一个来回加载验证码。redic默认get
    }
}
