package com.example.demo.RedisCache.imp.RedisService;

import com.example.demo.DataBase.Items;
import com.example.demo.DataBase.ToDoItem;

import java.util.Date;
import java.util.List;

public interface RedisCache{
    List<Items> getAllItemByDate(String date);
    List<Items> saveItem(String date);
    void deleteCache(String date);
}
