package com.example.demo;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

//单放一个配置启动类，扫其他java包的Bean对象
@Configuration
public class peizhiLei2 {
    @Bean
    public SimpleDateFormat simpleDateFormat(){//这句方法不是调用的，而是仅注入容器中了
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//year month day hour minute second
    }

}
