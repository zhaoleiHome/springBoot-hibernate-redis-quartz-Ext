package com.example.demo.Task;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class WeatherAutoWriter implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(WeatherAutoWriter.applicationContext==null){
            WeatherAutoWriter.applicationContext = applicationContext;
        }
    }
}
