package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = DemoApplication.class)//启动配置类获取
class DemoApplicationTests implements ApplicationContextAware{//获取容器

    /*测试类拿容器*/ private ApplicationContext rongqi;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {//参数接口，为了6大原则 √.f(√)
        rongqi = applicationContext;
    }
    @Test
    public void T1(){
        System.out.println(rongqi);
    }

    //测试是否在一次tomcat启动中是单例√：我猜对第二次启动就不同了！
    @Test
    public void T2(){
        //System.out.println(rongqi.getBean("mamiImp", MamiImp.class).add());//重命名后原名全部失效！
        System.out.println("____________测试是否在一次tomcat启动中是单例_______________");
        System.out.println(rongqi.getBean( "zhijie"));
    }

    //1接口下面2个实现类，且用接口autowire类型时：必须加
    @Autowired
    @Qualifier("mamiImp2")
    private Mami m;
    @Test
    public void T3(){
        m.add();
    }

    //必须在configuration新配置类中加
    @Autowired//虽配置类[已]放入容器中，还要auto拿出来给成员变量。。
    private SimpleDateFormat s;
    @Test
    public void T4(){
        System.out.println(s.format(new Date()));
    }
}
