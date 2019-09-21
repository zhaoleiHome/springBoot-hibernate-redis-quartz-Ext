package com.example.demo.DataBase.im;

import com.example.demo.DataBase.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherInfo extends JpaRepository<Weather,Long> {
}
