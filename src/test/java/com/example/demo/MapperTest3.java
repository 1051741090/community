package com.example.demo;


import com.nowcoder.community.b_dao.UserMapper1;
import com.nowcoder.community.a_entity.User;
import com.nowcoder.community.qidong;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes= qidong.class)
public class MapperTest3 {
    @Autowired//MB2 注入mapper
    private UserMapper1 userMapper;


    @Test
    public void T(){
        //查
        System.out.println(userMapper.selectById(111));
        //+
        User user = new User();user.setUsername("test");user.setPassword("123456");user.setSalt("abc");user.setEmail("test@qq.com");user.setHeaderUrl("http://www.nowcoder.com/101.png");user.setCreateTime(new Date());
        userMapper.insertUser(user);//这个自增插返回的是1成功，null失败
        System.out.println(user.getId());//查询结果自动放在user里了！
        System.out.println(userMapper.selectById(user.getId()));
        System.out.println(userMapper.selectByName("xxx"));
        //下面这个改对:refile有重复->*自动
        System.out.println(userMapper.selectByEmail("nowcoder149@sina.com"));

    }
    @Test
    public void T2(){
        //改
        userMapper.updateStatus(150,1);
        System.out.println(userMapper.updatePassword(150, "123"));
        System.out.println(userMapper.updateHeader(150, "http://www.nowcoder.com/102.png"));

    }
}
