package com.example.demo.Component;

import com.example.demo.DataBase.im.AddToDoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BeforeApplicationStart {
    @Autowired
    private AddToDoItem addToDoItem;

    @PostConstruct
    public void loadRedis(){

    }
}
