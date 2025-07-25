package com.demetriusdemiurge.t1_homework_kafka.service;

import com.demetriusdemiurge.t1_homework_kafka.dto.WeatherData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherProducer {

    @Value("${app.kafka.weather-topic}")
    private String weatherTopic;

    private final KafkaTemplate<String, WeatherData> kafkaTemplate;
    private final Random random = new Random();
    private final List<String> cities = List.of("Москва", "Рязань", "Санкт-Петербург", "Магадан", "Тюмень", "Чукотка АО", "Краснодар");
    private final List<String> conditions = List.of("солнечно", "облачно", "дождь");
    private int day = 0;


    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.SECONDS)
    public void generateAndSendWeatherData() {
        String city = cities.get(random.nextInt(cities.size()));
        String condition = conditions.get(random.nextInt(conditions.size()));

        if (day == 7) day = 0;

        double temperature = random.nextDouble() * 35.0;

        LocalDateTime msgDateTime = LocalDateTime.now();

        LocalDateTime weatherDateTime = msgDateTime
                .plusDays(day - 7)
                .minusHours(random.nextInt(msgDateTime.getHour()))
                .minusMinutes(random.nextInt(60))
                .minusSeconds(random.nextInt(60));

        day++;

        WeatherData data = new WeatherData(city, Math.round(temperature * 10.0) / 10.0, condition, weatherDateTime, msgDateTime);

        log.info("Отправка данных о погоде: {}", data);
        kafkaTemplate.send(weatherTopic, data.city(), data);
    }

}
