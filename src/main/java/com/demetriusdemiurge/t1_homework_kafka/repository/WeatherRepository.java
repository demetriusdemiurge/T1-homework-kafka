package com.demetriusdemiurge.t1_homework_kafka.repository;

import com.demetriusdemiurge.t1_homework_kafka.dto.WeatherData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WeatherRepository {

    private final Map<String, List<WeatherData>> weatherDataByCity = new ConcurrentHashMap<>();

    public void addWeatherData(String city, WeatherData weatherData) {
        weatherDataByCity.computeIfAbsent(city, k -> new ArrayList<>()).add(weatherData);
    }

    public List<WeatherData> getWeatherData(String city) {
        return weatherDataByCity.get(city);
    }

    public Map<String, List<WeatherData>> getAllWeatherData() {
        return weatherDataByCity;
    }

    public void clearAll() {
        weatherDataByCity.clear();
    }


}
