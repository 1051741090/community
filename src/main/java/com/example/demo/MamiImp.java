package com.example.demo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("zhijie")//重命名后，首小写类名全失效
public class MamiImp implements Mami {
    @Override
    public String add() {
        System.out.println("Dao接口拿到");
        return "接口拿到";
    }
}
