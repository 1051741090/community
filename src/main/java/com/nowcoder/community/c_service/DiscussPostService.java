package com.nowcoder.community.c_service;

import com.nowcoder.community.a_entity.DiscussPost;
import com.nowcoder.community.b_dao.DiscussPostMapper1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper1 discussPostMapper;

    //没业务，直接返数据库查的
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }

    public int findDiscussPostRows(int userid){
        return discussPostMapper.selectDiscussPostRows(userid);//userid=0时，默认查所有
    }
}