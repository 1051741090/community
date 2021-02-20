package com.nowcoder.community.b_dao;

import com.nowcoder.community.a_entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;

@Mapper
@Controller
public interface UserMapper1 {//MB1:面向接口编程-产品经理
    int insertUser(User user);
    //传2个基本类型时spring区分不开：必须用注解声明
    int updateStatus(@Param("id")int id, @Param("status")int status);
    int updateHeader(@Param("id")int id, @Param("headerUrl")String headerUrl);
    int updatePassword(@Param("id")int id, @Param("password")String password);


    //目前只教返回一行数据，！必须一行！
    User selectById(int id); //一个参数不用加@P,可以区分
    User selectByName(String username);
    User selectByEmail(String email);
}
