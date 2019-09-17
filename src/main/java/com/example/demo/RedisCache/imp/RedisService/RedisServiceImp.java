package com.example.demo.RedisCache.imp.RedisService;

import com.example.demo.DataBase.Items;
import com.example.demo.DataBase.ToDoItem;
import com.example.demo.DataBase.im.AddItem;
import com.example.demo.DataBase.im.AddToDoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@CacheConfig()
public class RedisServiceImp implements RedisCache {
    @Autowired
    private AddToDoItem addToDoItem;
    @Autowired
    private AddItem addItem;

    @Override
    @Cacheable(value = "get-todo-item",key="#id")
    public ToDoItem getToDoItemById(Integer id) {
        System.out.println("查询事项");
        return addToDoItem.findById(id);
    }

    @Override
    @Cacheable(value = "get-item",key = "#date")
    public List<Items> getAllItemByDate(String date) {
        System.err.println("获取缓存");
        return addItem.findByAppDate(date);
    }

    @Override
    @CachePut(value = "get-item",key = "#date")
    public List<Items> saveItem(String date) {
        System.err.println("保存的方法");
        return addItem.findByAppDate(date);
    }

    @CacheEvict(value = "get-item",allEntries = false)
    @Override
    public void deleteCache(String date) {
        System.err.println("删除成功");
    }
}












