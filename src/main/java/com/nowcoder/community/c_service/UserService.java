package com.nowcoder.community.c_service;

import com.nowcoder.community.a_entity.User;
import com.nowcoder.community.b_dao.UserMapper1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper1 mapper;

    public User findUserByid(int id){
        return mapper.selectById(id);
    }
}

