package com.example.demo;


import com.nowcoder.community.a_entity.DiscussPost;
import com.nowcoder.community.b_dao.DiscussPostMapper1;
import com.nowcoder.community.c_service.DiscussPostService;
import com.nowcoder.community.qidong;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = qidong.class)
public class DiscussPostTest3 {
    //1注入mapper
    @Autowired
    private DiscussPostMapper1 discussPostMapper;

    @Test
    public void T(){
        System.out.println(discussPostMapper.selectDiscussPostRows(0));
        //101号用户的0-5条题解
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(101, 0, 5);
        for(DiscussPost d: list) System.out.println(d); }

    @Autowired
    private DiscussPostService discussPostService;

    @Test
    public void T2(){
        System.out.println(discussPostService.findDiscussPosts(101, 0, 5));//userId是SYSTEM管理员没发过贴
    }

}
