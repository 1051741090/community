package com.nowcoder.community.z_util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

//邮件工具类
@Component
public class MailCilent {
    public static final Logger logger = LoggerFactory.getLogger(MailCilent.class);

    @Value("${spring.mail.username}")//从app.prop中直接拿配置的名字
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;//java自带封装邮件content内容的包

    //这个是工具方法，controll中直接调，不用网页访问之类的~
    //发给谁，题目，内容
    public void sendMail(String to,String subject, String text){
        MimeMessageHelper helper = new MimeMessageHelper(javaMailSender.createMimeMessage());
        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            //内容封装完毕，发送
            javaMailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("邮件发送失败，日志已记入桌面。"+e.getMessage());
        }

    }
}
