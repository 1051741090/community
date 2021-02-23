package com.nowcoder.community.b_dao;

import com.nowcoder.community.a_entity.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LoginTicketMapper {


    // 插入新用户登录票据
    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values (#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")//这两句是一个事：先定自增之后告诉自增的名字。（int是在sql中的，insert返还成功失败）
    // 【若select count 即select后面单变量，int返回这个单变量】
    int insertLoginTicket(LoginTicket loginTicket);


    //查询用票码，查询是否存在
    @Select({
            "select * from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(@Param("ticket")String ticket);

    //更新票据失效status0->1【无selcet 列名，则是返还10成功与否？】


    @Update({
            "update login_ticket set status=#{status} where ticket=#{ticket}"
    })
    int updateStatus(@Param("ticket")String ticket, @Param("status")int status);//up要先定位，后改
}
