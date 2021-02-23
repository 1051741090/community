package com.nowcoder.community.z_util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>——————————————————————————————————————————————-————
 * <p>   【名字】登录注解 【所属包】com.nowcoder.community.z_util.annotation
 * <p>
 * <p>   【谁调用我】拦截器反射被标注的class
 * <p>
 * <p>   【调用我干什么】拦截未登录直接访问拼接的url
 * <p>——————————————————————————————————————————————-————
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {

}
