package com.nowcoder.community.z_util;

import com.nowcoder.community.a_entity.User;
import org.springframework.stereotype.Component;


/**
 * <p>——————————————————————————————————————————————-————
 * <p>   【名字】服务器永久变量 【所属包】com.nowcoder.community.z_util
 * <p>
 * <p>   【谁调用我】登票拦截器
 * <p>
 * <p>   【调用我干什么】用ThreadLocal存入服务器永久变量
 * <p>——————————————————————————————————————————————-————
 */
@Component//@放类注释下面才行
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();//这是个map能存多个user

    public User getUser(){
        return users.get();//这是TL的官方方法
    }
    public void setUser(User user){
        users.set(user);//别扭，放入一个user
    }

    public void clear(){
        //清空当前map
        users.remove();
    }
}
