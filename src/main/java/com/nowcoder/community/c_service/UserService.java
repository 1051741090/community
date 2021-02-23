package com.nowcoder.community.c_service;

import com.nowcoder.community.a_entity.LoginTicket;
import com.nowcoder.community.a_entity.User;
import com.nowcoder.community.b_dao.LoginTicketMapper;
import com.nowcoder.community.b_dao.UserMapper1;
import com.nowcoder.community.z_util.CommunityUtil;
import com.nowcoder.community.z_util.MailCilent;
import com.sun.org.apache.xml.internal.serializer.utils.SerializerMessages_zh_CN;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
//mhuserService
@Service
public class UserService {
    @Autowired
    private UserMapper1 mapper;

    //发邮件用
    @Value("${community.path.domain}")
    private String domain;// http://localhost:8080
    @Value("${server.servlet.context-path}")
    private String contextPath; // /r
    @Autowired
    private MailCilent mailCilent;
    @Autowired
    private TemplateEngine templateEngine;

    //插入用户
    @Autowired
    private UserMapper1 userMapper;


    public User findUserByid(int id){
        return mapper.selectById(id);
    }

    public Map<String,Object> register(User user) {
        //惊：报错才有map，map为空就正确了
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空格!!!!!");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空格!!!!!");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空格!!!!!!");
            return map;
        }
        //用户名判是否重复
        User u = userMapper.selectByName(user.getUsername());
        if(u!=null){
            map.put("usernameMsg", "用户名已经被注册过了！！");
            return map;
        }
        //数据库入一个未激活status=0的用户
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+ user.getSalt()));
        user.setType(0);
        user.setStatus(0);//核心：未激活
        user.setActivationCode(CommunityUtil.generateUUID());//激活时比对邮件是不是伪造的用
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        //插入！
        userMapper.insertUser(user);

        // 激活邮件:先html-context准备
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        context.setVariable("username", user.getUsername());
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();

        context.setVariable("url",url);
        mailCilent.sendMail(
                user.getEmail(),
                "牛客网讨论区激活邮件",
                templateEngine.process("moban/mail/activation", context)
        );//前面的/moban是为了告诉HTML合并的模板
        //【这个邮件发完，才会到COntroller那里返回页面，那里证明已经发送出邮件了】

        return null;//这是service不用返回/index
    }

    //激活改statu为1-返回结果给controller判
    public int jihuo(int userId, String code){
        User user = userMapper.selectById(userId);
        int s = user.getStatus();
        if(s==0){
            if(!code.equals(user.getActivationCode()))return 0;//卡激活失败

            userMapper.updateStatus(userId,1);
            return 1;
        }else if(s==1){
            return 2;
        }

        return -444;//不执行
    }





    //首次登录存入票据并返回ck，根据ck自动登录后期做
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    public Map<String, Object> login(String username, String password, int shixiaoTime) {
        Map<String,Object> map = new HashMap<>();

        map.put("f1",0);
        //1-用户名不存在
        User user = userMapper.selectByName(username);
        if(user==null){
            map.put("f1",1);
            return map;
        }
        //2-密码输入错误
        //此时user已经拿到了
        String tPas = CommunityUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(tPas)){
            map.put("f1",2);
            return map;
        }
        //3-账号未激活
        if(user.getStatus()==0){
            map.put("f1",3);
            return map;
        }


        //4 漏下来的王者，可以插入了新的登陆票据表中了！，并返回新生成的ticket票据为了control存ck
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());//生成0-1a-z的随机乱码
        map.put("ck",loginTicket.getTicket());//并存ck
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + shixiaoTime * 1000));
        //插入！
        loginTicketMapper.insertLoginTicket(loginTicket);
        return map;
    }

    public void logout(String ticket) {
        //用tick定位，改1即可，随意1不用做参数（若多种状态status要传）
        loginTicketMapper.updateStatus(ticket,1);//1票据失效
    }



    /**
     * <p>——————————————————————————————————————————————-————
     * <p>   【当前类】UserService
     * <p>
     * <p>   【谁调用我】登票拦截器
     * <p>
     * <p>   【调用我干什么】
     * <p>   用 String ticket
     * <p>   在 登票数据库里找是否有这行
     * <p>   返 LoginTicket
     * <p>——————————————————————————————————————————————-————
     */
    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }
    /**
     * <p>——————————————————————————————————————————————-————
     * <p>   【当前类】UserService
     * <p>
     * <p>   【谁调用我】UserController修改图片的控制器
     * <p>
     * <p>   【调用我干什么】用userid找位置，之后改newURL
     * <p>——————————————————————————————————————————————-————
     */
    public void updateHeader(int id, String newimgURL) {
        //惊：user的pojo只有id，没有userid（当然没有！，这个id是别的userid的外键）
        mapper.updateHeader(id,newimgURL);
        //先处理原密码错误，返回passM;

    }

    /**
     * <p>——————————————————————————————————————————————-————
     * <p>   【当前类】UserService
     * <p>
     * <p>   【谁调用我】个人设置修改改密码提交按钮
     * <p>
     * <p>   【调用我干什么】改密码（pass+salt）->md5
     * <p>——————————————————————————————————————————————-————
     */
    public void updatePasswordByid(int id, String newPassword){
        mapper.updatePassword(id,newPassword);
    }
}

