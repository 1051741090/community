package com.nowcoder.community.d_controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.a_entity.User;
import com.nowcoder.community.c_service.UserService;
import com.nowcoder.community.z_util.CHANGSHU;
import com.nowcoder.community.z_util.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

@Controller
public class LoginController {
    //mh 登录login控制器
//    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @RequestMapping(path = "/register",method = RequestMethod.GET)//这个get很关键，&识别出第一次访问注册，跳转到表单页面
    public String zhuanDie(Model model){
        return "moban/site/register";
    }

    @Autowired
    private UserService userService;
    //先接收一下表单信息
    @RequestMapping(path = "/register" ,method = RequestMethod.POST)
    public String register(Model model, User user){//user智能set拿到表单传的
        Map<String,Object> map = userService.register(user);
        if(map!=null){
            //返回提示注册表单错误信息
            //用model传给对应html
            model.addAttribute("usernameMsg",map.get("usernameMsg"));//放一起一顿弹？
            model.addAttribute("passwordMsg",map.get("passwordMsg"));//放一起一顿弹？
            model.addAttribute("emailMsg",map.get("emailMsg"));//放一起一顿弹？
            return "moban/site/register";//这个死/moban/site/rister.html：是注册表单，直接/reg不是.html是个javaClass指向
        }else if(map==null){

            //正常无错:提示去点击激活邮件
            model.addAttribute("jihuoM","我们已发送一封激活邮件，请点击。【爱激活不激活，爱点不点我不管】（该页面可忽略，无具体意义）");
            return "moban/site/operate-result";
        }
        return "1over";//没写body的String函数，必须r一个确定html；
    }




    //点击邮件中的激活链接
    // http://localhost:8080/community/activation/101/code
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String jihuoC(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code){
        //jihuoController
        int sucOrNot = userService.jihuo(userId, code);
        if(sucOrNot==1){
            model.addAttribute("msg","您的账号激活成功");
        }else if(sucOrNot==0){
            model.addAttribute("msg","【您应该是直接浏览器敲的url】您的邮件是伪造的，邮件链接处的激活码和后台数据库激活码不对应，激活失败");
        }else if(sucOrNot==2){
            //已经激活过是status=1
            model.addAttribute("msg","您的以前激活过了，不要重复操作！");
        }
        return "moban/site/operate-result";
    }


    //返回验证码图片，不能用String，要用response
    @Autowired
    private Producer createKap;//可以传方法！！而且方法名就是返回结果
    //静态页面的 th:src的@{/kaptcher}会自动调
    @RequestMapping(path="/kaptcha", method = RequestMethod.GET)
    public void f3(HttpServletResponse response, HttpSession session){//controller的方法名字无用

        //调用静态config.Bean
        String text = createKap.createText();//先text
        BufferedImage image = createKap.createImage(text);

        //存session，维护最后一次刷新的文本【不要图片】，为后期登录对比用
        session.setAttribute("kaptcha",text);

        //h：返回浏览器显示
        response.setContentType("image/png");
        try {
            ImageIO.write(image,"png",response.getOutputStream());
        } catch (IOException e) {
//            logger.error("验证码图片产生出错："+e.getMessage());
            e.getMessage();
        }
        //response塞得不用r
    }


    //登录【第一次空登，转发302】
    //首次点击为了模板调用生成验证码@,空转get一次
    //必须参数写Movel，要不当成body报错
    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String firstDianDengLu(Model model){
        return "moban/site/login"; }

    @Autowired
    private CHANGSHU changshu;


    //登录【第二次真正post提交表单】
    @RequestMapping(path = "/login" ,method = RequestMethod.POST)
    public String dengluSumit(Model model,
                              String username, String password, String code,
                              boolean jizhuwo,
                              HttpServletResponse response, HttpSession session){

        //1 验证验证码【不用调mapper，可以直接从唯一会话ses拿，所以直接在controll里写】
        String k = (String)session.getAttribute("kaptcha");

        //2 检查账号密码->涉及写service的调用mapper
        int shixiaoTime = changshu.DEFAULT_EXPIRED_SECONDS;
        if(jizhuwo==true)shixiaoTime = changshu.REMEMBER_EXPIRED_SECONDS;
        //先处理表单数据有问题的情况
        //1空白
        //缺了一步：验证码session拿出来看看
        String kap = (String)session.getAttribute("kaptcha");
        if(StringUtils.isBlank(username)|| StringUtils.isBlank(username) ||StringUtils.isBlank(code) || !kap.equalsIgnoreCase(code)){
            if (StringUtils.isBlank(username)) {
                model.addAttribute("usernameM", "用户名不能为空格!");
            }
            if (StringUtils.isBlank(password)) {
                model.addAttribute("passwordM", "密码不能为空格!");
            }
            if(StringUtils.isBlank(code)){
                model.addAttribute("codeM","验证码不能是空格！！");
            }

            if( !kap.equalsIgnoreCase(code))
                model.addAttribute("codeM","验证码输入错误，仔细看看！！");

            return "moban/site/login";//一起return，要不每次只提醒红一栏
        }

        //2mapper查询库内深层问题
        Map<String, Object> res = userService.login(username, password, shixiaoTime);
        //1-用户名不存在//2-密码输入错误//3-账号未激活
        if((int)res.get("f1")!=0){
            if((int)res.get("f1")==1) model.addAttribute("usernameM", "查无此用户名！");
            if((int)res.get("f1")==2) model.addAttribute("passwordM", "查到用户名了，但是密码不对！");
            if((int)res.get("f1")==3) model.addAttribute("usernameM", "查到用户名了，但是该用户没点激活邮件去激活status");

            return "moban/site/login";//一起return?
        }else {

            //f1==标记位=0，证明已经新插入到登陆票据表中了

            //这里应该加一句如果mapper查到登票表中有tick，就返回已有tic，并且不执行insert语句-》这样实现不重复加？~。

            //qc：存cok-1
            String ticket = (String)res.get("ck");
            Cookie cookie = new Cookie("ticket",ticket);
            cookie.setPath("/r");//死，必须配置cok存在范围
            cookie.setMaxAge(shixiaoTime);
            //response带ck回去
            response.addCookie(cookie);

            //要访问javaindex 不访问模板，因为首页要先去后台拿所有讨论帖
            return "redirect:/index";//template原来这样实现复用！
        }



    }

}
