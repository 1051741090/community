package com.example.demo;


import org.springframework.stereotype.Controller;

@Controller
public class MamiImp2 implements Mami {
    @Override
    public String add() {
        System.out.println("区分开mami2了！");
        return "";
    }
}
