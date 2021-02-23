package com.nowcoder.community.x_config;

import com.nowcoder.community.d_controller.Lanjieqi.LoginRequiredInterceptor;
import com.nowcoder.community.d_controller.Lanjieqi.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class peizhiLanJieQi implements WebMvcConfigurer{

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;//这里爆红不用管，是对的

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("**/*.css", "**/*.js", "**/*.png", "**/*.jpg", "**/*.jpeg");
        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("**/*.css", "**/*.js", "**/*.png", "**/*.jpg", "**/*.jpeg");
    }
}

