package com.example.demo.RedisCache.imp.RedisService;

import com.example.demo.DataBase.ToDoItem;
import com.example.demo.DataBase.im.AddToDoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = "todo_item")
public class RedisServiceImp implements RedisCache {
    @Autowired
    private AddToDoItem addToDoItem;

    @Override
    @Cacheable(key="#id")
    public ToDoItem getToDoItemById(Integer id) {
        System.out.println("查询事项");
        return addToDoItem.findById(id);
    }

    @Override
    public List<ToDoItem> getAllToDoItemByDate(Date date) {
        System.err.println("获取当天时间的缓存");
        return null;
    }
}












