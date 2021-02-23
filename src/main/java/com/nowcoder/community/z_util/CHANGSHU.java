package com.nowcoder.community.z_util;


import org.springframework.stereotype.Component;

@Component
public class CHANGSHU {
    /**
     * 默认状态的登录凭证的超时时间
     */
    public static int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住状态的登录凭证超时时间
     */
    public static int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

}
