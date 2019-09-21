package com.example.demo.DataBase;

import javax.persistence.*;

@Entity
@Table(name = "weather")
public class Weather {
    @Id
    @Column(name = "id",unique = true,nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "weather_info")
    private String weather_info;
    @Column(name = "date")
    private String date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWeather_info() {
        return weather_info;
    }

    public void setWeather_info(String weather_info) {
        this.weather_info = weather_info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
