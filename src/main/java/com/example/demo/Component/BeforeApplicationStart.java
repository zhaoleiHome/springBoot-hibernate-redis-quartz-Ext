package com.example.demo.Component;

import com.example.demo.DataBase.im.AddItem;
import com.example.demo.DataBase.im.AddToDoItem;
import com.example.demo.DataBase.im.TaskNote;
import com.example.demo.RedisCache.imp.RedisService.RedisServiceImp;
import com.example.demo.Task.JobTaskUtils;
import com.example.demo.Task.TaskDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BeforeApplicationStart implements ApplicationRunner {
    public final String NOTE_INFO_KEY = "note_info_key";
    public final String WEATHER_INFO_KEY="weather_info_key";
    @Autowired
    private AddToDoItem addToDoItem;
    @Resource
    private RedisTemplate<String, List> redisTemplate;
    @Autowired
    private AddItem addItem;
    @Autowired
    private RedisServiceImp redisServiceImp;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private JobTaskUtils jobTaskUtils = new JobTaskUtils();
    @Autowired
    private TaskNote taskNote;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        redisServiceImp.deleteCache(NOTE_INFO_KEY);
        redisServiceImp.saveItem(date,NOTE_INFO_KEY);
        redisServiceImp.deleteCacheByWeather(WEATHER_INFO_KEY);
        redisServiceImp.saveWeatherInfo(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),WEATHER_INFO_KEY);
        //开启任务，将任务添加到内存中
        jobTaskUtils.taskDemo("jobDemoTask","jobDemoTaskGroup",
                "triggerDemo","triggerDemoGroup","0 0 * * * ? *",TaskDemo.class,taskNote);
    }
}
