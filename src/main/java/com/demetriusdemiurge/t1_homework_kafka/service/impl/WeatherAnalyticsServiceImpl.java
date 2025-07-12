package com.demetriusdemiurge.t1_homework_kafka.service.impl;

import com.demetriusdemiurge.t1_homework_kafka.dto.WeatherData;
import com.demetriusdemiurge.t1_homework_kafka.repository.WeatherRepository;
import com.demetriusdemiurge.t1_homework_kafka.service.WeatherAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherAnalyticsServiceImpl implements WeatherAnalyticsService {

    private final WeatherRepository weatherRepository;

    @Override
    public void processMessage(WeatherData data) {
        weatherRepository.addWeatherData(data.city(), data);
    }

    @Override
    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.SECONDS)
    public void printAnalyticsSummary() {
        if (weatherRepository.getAllWeatherData().isEmpty()) {
            log.info("Аналитика: данных для анализа пока нет.");
            return;
        }

        log.info("------ Еженедельная сводка погоды ------");
        findMostRainyCity();
        findHottestDay();
        findLowestAverageTempCity();
        log.info("----------------------------------------\n");

        weatherRepository.clearAll();
    }

    @Override
    public void findMostRainyCity() {
        Optional<Map.Entry<String, Long>> result = weatherRepository.getAllWeatherData().entrySet().stream()
                .map(entry -> {
                    long rainyDays = entry.getValue().stream()
                            .filter(data -> "дождь".equals(data.condition()))
                            .map(WeatherData::weatherDate)
                            .distinct()
                            .count();
                    return Map.entry(entry.getKey(), rainyDays);
                })
                .max(Map.Entry.comparingByValue());

        result.ifPresent(entry -> {
            if (entry.getValue() > 0) {
                log.info("Аналитика: Самый дождливый город - {} ({} д.)", entry.getKey(), entry.getValue());
            } else {
                log.info("Аналитика: Дождливых дней на этой неделе не было.");
            }
        });
    }

    @Override
    public void findHottestDay() {
        Optional<WeatherData> hottest = weatherRepository.getAllWeatherData().values().stream()
                .flatMap(List::stream)
                .max(Comparator.comparingDouble(WeatherData::temperature));

        hottest.ifPresent(data -> log.info("Аналитика: Самая высокая температура - {}°C в городе {} ({})",
                data.temperature(), data.city(), data.weatherDate()));
    }

    @Override
    public void findLowestAverageTempCity() {
        Optional<Map.Entry<String, Double>> result = weatherRepository.getAllWeatherData().entrySet().stream()
                .map(entry -> {
                    double avgTemp = entry.getValue().stream()
                            .mapToDouble(WeatherData::temperature)
                            .average()
                            .orElse(0.0);
                    return Map.entry(entry.getKey(), avgTemp);
                })
                .min(Map.Entry.comparingByValue());

        result.ifPresent(entry -> log.info("Аналитика: Самая низкая средняя температура в городе {} ({}°C)",
                entry.getKey(), Math.round(entry.getValue() * 10.0) / 10.0));
    }

}