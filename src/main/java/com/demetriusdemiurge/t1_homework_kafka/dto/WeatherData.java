package com.demetriusdemiurge.t1_homework_kafka.dto;

import java.time.LocalDateTime;

public record WeatherData(
        String city,
        double temperature,
        String condition,
        LocalDateTime weatherDateTime,
        LocalDateTime msgDateTime
) {}