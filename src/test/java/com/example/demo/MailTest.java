package com.example.demo;

import com.nowcoder.community.qidong;
import com.nowcoder.community.z_util.MailCilent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes= qidong.class)
public class MailTest {
    @Autowired
    private MailCilent mailCilent;
    @Test
    public void T(){
        mailCilent.sendMail("1051741090@qq.com","测试2","oxixiix");
    }

    //在test中不用RM("/")访问temple的另种方法
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void sendHtmlMail(){
        //发送给模板-easy！
        Context context = new Context();
        context.setVariable("name","李赢");//传给html的map-k:v
        //发送
        mailCilent.sendMail("1051741090@qq.com","测试2",templateEngine.process("moban/mail/1",context));//这里告诉发送到哪[这玩意大神也是抄的！背不下来的]

    }

}
