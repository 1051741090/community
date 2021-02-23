package com.nowcoder.community.d_controller.Lanjieqi;


import com.nowcoder.community.a_entity.LoginTicket;
import com.nowcoder.community.a_entity.User;
import com.nowcoder.community.c_service.UserService;
import com.nowcoder.community.z_util.CookieUtil;
import com.nowcoder.community.z_util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@Controller
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;




    /**
     * <p>——————————————————————————————————————————————-————
     * <p>   【当前类】LoginTicketInterceptor
     * <p>
     * <p>   【谁调用我】所有访问java的RM"/xxx“
     * <p>
     * <p>   【调用我干什么】
     * <p>   进入你给的RM/xxx前先看看符不符合我的要求（有cok），符合我的要求就java代码TL存user，之后再走你的RM/xxx
     * <p>——————————————————————————————————————————————-————
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getValue(request, "ticket");
        if(ticket!=null){
            //先拿凭证，看这个ck的status是否退出失效的status=1
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //错1：这里超时了所以有ck也没拿到【其实应该给客户机的ck设置一个自毁时间（但客户机有杀毒不让你注入脚本。。）】
            if(loginTicket!=null && loginTicket.getStatus()==0 && loginTicket.getExpired().after(new Date())){
                //1 用票先查用户id，再用userid查出user表的这行。
                int curUserId = loginTicket.getUserId();
                User user = userService.findUserByid(curUserId);
                //2 放入线程[几个来回都能用了，反正在服务器内。但注意是接请求|发响应，发请求不管]！！！！！！！！！！！！！
                hostHolder.setUser(user);
            }
        }
        return true;//没啥用r就行
    }
    /**
     * <p>——————————————————————————————————————————————-————
     * <p>   【当前类】LoginTicketInterceptor
     * <p>
     * <p>   【谁调用我】pre拦截完之后，服务器temp模板之前拦截
     * <p>
     * <p>   【调用我干什么】user放进model内，因为只有post能访问model（所以不能再pre中执行）
     * <p>
     * <p>   【其他】后期用的时候叫什么名字的变量：已经登录的user叫loginUser，在服务器端
     * <p>——————————————————————————————————————————————-————
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user!=null && modelAndView!=null){
            modelAndView.addObject("loginUser",user);//已经登录的user叫loginUser，在服务器端
        }
    }
        /**
         * <p>——————————————————————————————————————————————-————
         * <p>   【当前类】LoginTicketInterceptor
         * <p>
         * <p>   【谁调用我】服务器tem模板生成完毕，响应信号刚发送就拦截
         * <p>
         * <p>   【调用我干什么】删除服务器java线程中的user。（没事，下次request请求又塞入了）
         * <p>——————————————————————————————————————————————-————
         */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
