package com.example.demo.Component;

import com.example.demo.DataBase.im.AddItem;
import com.example.demo.DataBase.im.AddToDoItem;
import com.example.demo.DataBase.im.TaskNote;
import com.example.demo.DataBase.im.WeatherInfo;
import com.example.demo.RedisCache.imp.RedisService.RedisServiceImp;
import com.example.demo.Task.JobTaskUtils;
import com.example.demo.Task.TaskDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
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
    @Autowired
    private WeatherInfo weatherInfo;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        redisServiceImp.deleteCache(NOTE_INFO_KEY);
        redisServiceImp.saveItem(date,NOTE_INFO_KEY);
        redisServiceImp.deleteCacheByWeather(WEATHER_INFO_KEY);
        redisServiceImp.saveWeatherInfo(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),WEATHER_INFO_KEY);
        this.findWeatherByToday();
        //开启任务，将任务添加到内存中
        jobTaskUtils.taskDemo("jobDemoTask","jobDemoTaskGroup",
                "triggerDemo","triggerDemoGroup","0 0 * * * ? *",TaskDemo.class,taskNote);
    }
    private void findWeatherByToday(){
       List list = weatherInfo.findByDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
       if(list==null||list.size()<=0){
           List listPar = new ArrayList();
           listPar.add(new ByteArrayHttpMessageConverter());
           listPar.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
           listPar.add(new ResourceHttpMessageConverter());
           RestTemplate restTemplate = new RestTemplate(listPar);
           String weather = restTemplate.getForObject("http://www.weather.com.cn/data/cityinfo/101120101.html",String.class);
           new TaskDemo().saveWeather(weather);
       }
    }
}
