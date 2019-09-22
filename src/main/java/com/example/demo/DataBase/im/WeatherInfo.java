package com.example.demo.DataBase.im;

import com.example.demo.DataBase.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WeatherInfo extends JpaRepository<Weather,Long> {
    @Query(nativeQuery = true,value = ("select * from weather w where TO_DAYS(date) = TO_DAYS(now())"))
    List<Weather> findByDate(String Date);
}
