package com.example.demo;

import com.nowcoder.community.a_entity.LoginTicket;
import com.nowcoder.community.b_dao.LoginTicketMapper;
import com.nowcoder.community.qidong;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes= qidong.class)
public class TicketMapperTest {

    @Autowired
    private LoginTicketMapper mapper;

    @Test
    public void T(){
        //测试插入登录票据
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
        //插入
        mapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void T2(){
        //测试用票据查指定行
        System.out.println(mapper.selectByTicket("abc"));
        //测试修改status票据有效性标志位0—>1
        mapper.updateStatus("abc",1);
    }
}
