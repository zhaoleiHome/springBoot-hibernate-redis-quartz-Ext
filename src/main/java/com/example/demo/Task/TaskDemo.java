package com.example.demo.Task;

import com.example.demo.DataBase.Weather;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class TaskDemo implements Job{
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List list = new ArrayList();
        list.add(new ByteArrayHttpMessageConverter());
        list.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        list.add(new ResourceHttpMessageConverter());
        RestTemplate restTemplate = new RestTemplate(list);
        String weather = restTemplate.getForObject("http://www.weather.com.cn/data/cityinfo/101120101.html",String.class);
        this.saveWeather(weather);
    }
    public void saveWeather(String weatherInfo){
        System.err.println("25524");
        Integer temp = null;
        Connection connection = null;
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?serverTimezone=UTC&characterEncoding=utf-8","root","");
            String querySql = "select * from weather where TO_DAYS(date) = TO_DAYS(now())";
            PreparedStatement queryPre = connection.prepareStatement(querySql);
            ResultSet result = queryPre.executeQuery();
            temp=0;
            while (result.next()){
                    temp = 1;
            }
            String Sql = null;
            if(temp==0){
                Sql = "insert into weather(weather_info,date) value(?,?)";
            }else{
                Sql = "update weather set weather_info = ? where date = ?";
            }
            PreparedStatement statement = connection.prepareStatement(Sql);
            statement.setString(1,weatherInfo);
            statement.setString(2,new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            int rowCunt = statement.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
