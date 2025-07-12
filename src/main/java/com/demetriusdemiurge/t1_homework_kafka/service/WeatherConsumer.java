package com.demetriusdemiurge.t1_homework_kafka.service;


import com.demetriusdemiurge.t1_homework_kafka.dto.WeatherData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherConsumer {

    private final WeatherAnalyticsService analyticsService;

    @KafkaListener(topics = "${app.kafka.weather-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeWeatherData(WeatherData data) {
        log.info("Получены данные о погоде: {}", data);
        analyticsService.processMessage(data);
    }

}
