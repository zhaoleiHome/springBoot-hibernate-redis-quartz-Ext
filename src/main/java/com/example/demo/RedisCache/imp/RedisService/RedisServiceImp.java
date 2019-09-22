package com.example.demo.RedisCache.imp.RedisService;

import com.example.demo.DataBase.Items;
import com.example.demo.DataBase.ToDoItem;
import com.example.demo.DataBase.Weather;
import com.example.demo.DataBase.im.AddItem;
import com.example.demo.DataBase.im.AddToDoItem;
import com.example.demo.DataBase.im.WeatherInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("cache_util")
@CacheConfig()
public class RedisServiceImp implements RedisCache {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AddToDoItem addToDoItem;
    @Autowired
    private AddItem addItem;
    @Autowired
    private WeatherInfo weatherInfo;

    @Override
    @Cacheable(value = "get-item",key = "#key")
    public List<Items> getAllItemByDate(String date,String key) {
        return addItem.findByAppDate(date);
    }

    @Override
    @CachePut(value = "get-item",key = "#key")
    public List<Items> saveItem(String date,String key) {
        logger.info("保存笔记信息到缓存成功");
        return addItem.findByAppDate(date);
    }

    @CacheEvict(value = "get-item",allEntries = false)
    @Override
    public void deleteCache(String key) {
        logger.info("删除笔记缓存内容成功");
    }

    @CachePut(value = "weather_info",key = "#key")
    @Override
    public List<Weather> saveWeatherInfo(String date,String key){
        logger.info("保存天气信息到缓存成功");
        return weatherInfo.findByDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }
    @Cacheable(value = "weather_info",key = "#key")
    @Override
    public List<Weather> getWeatherInfo(String date,String key){
        return weatherInfo.findByDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }
    @CacheEvict(value = "weather_info",allEntries = false)
    @Override
    public void deleteCacheByWeather(String key){
        logger.info("删除天气信息成功");
    }
}












