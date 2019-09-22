package com.example.demo.RedisCache.imp.RedisService;

import com.example.demo.DataBase.Items;
import com.example.demo.DataBase.ToDoItem;
import com.example.demo.DataBase.Weather;

import java.util.Date;
import java.util.List;

public interface RedisCache{
    List<Items> getAllItemByDate(String date,String key);
    List<Items> saveItem(String date ,String key);
    void deleteCache(String date);
    List<Weather> saveWeatherInfo(String date,String tempStr);
    List<Weather> getWeatherInfo(String date,String tempStr);
    void deleteCacheByWeather(String tempStr );
}
