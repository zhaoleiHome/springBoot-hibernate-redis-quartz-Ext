package com.example.demo.RedisCache.imp.RedisService;

import com.example.demo.DataBase.ToDoItem;

import java.util.Date;
import java.util.List;

public interface RedisCache{
    ToDoItem getToDoItemById(Integer id);
    List<ToDoItem> getAllToDoItemByDate(Date date);
}
