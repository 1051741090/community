package com.nowcoder.community.d_controller.Lanjieqi;

import com.nowcoder.community.z_util.HostHolder;
import com.nowcoder.community.z_util.annotation.LoginRequired;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * <p>——————————————————————————————————————————————-————
 * <p>   【名字】检查登录注解拦截器 【所属包】com.nowcoder.community.d_controller.Lanjieqi
 * <p>
 * <p>   【谁调用我】登录注解
 * <p>
 * <p>   【调用我干什么】//拿到注解标识的类之后，怎么处理pre（用独立的host rep req se ck判断）
 * <p>——————————————————————————————————————————————-————
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor{

    @Autowired
    private HostHolder hostHolder;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
        if(handler instanceof HandlerMethod){
            Method method = ((HandlerMethod) handler).getMethod();
            //拿到注解标识的类
            LoginRequired loginRequired =  method.getAnnotation(LoginRequired.class);
            //先识别标识了require的类 & 在判断当前如果是没有登录
            if(loginRequired!=null && hostHolder.getUser()==null){
                response.sendRedirect(request.getContextPath()+"/login");//这不是RM+model，所以要用response（其实return"/index"底层用的这个方法）
                return false;//拒绝访问原url
            }
        }
        return true;//处理完继续访问你给的url
    }

}
