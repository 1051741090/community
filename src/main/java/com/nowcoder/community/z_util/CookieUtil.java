package com.nowcoder.community.z_util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>   【名字】cookie工具类
 * <p>
 * <p>   【谁调用我】登票拦截器
 * <p>
 * <p>   【调用我干什么】
 * <p>   用 request和name
 * <p>   在 cookie里拿叫name的内容
 * <p>   返 内容
 */
@Component
public class CookieUtil {
    public static String getValue(HttpServletRequest request, String name){
        if(request==null|| name==null){
            throw new IllegalArgumentException("参数传入出异常导致终止程序：请求为空，或ck名为空");
        }

        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie c:cookies){
                if(c.getName().equals(name)) return c.getValue();
            }
        }
        return null;
    }
}
