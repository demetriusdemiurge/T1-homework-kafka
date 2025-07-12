package com.demetriusdemiurge.t1_homework_kafka.service;

import com.demetriusdemiurge.t1_homework_kafka.dto.WeatherData;

public interface WeatherAnalyticsService {

    void processMessage(WeatherData data);

    void printAnalyticsSummary();

    void findMostRainyCity();

    void findHottestDay();

    void findLowestAverageTempCity();

}