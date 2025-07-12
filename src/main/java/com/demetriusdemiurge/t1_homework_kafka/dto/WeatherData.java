package com.demetriusdemiurge.t1_homework_kafka.dto;

import java.time.LocalDate;

public record WeatherData(
        String city,
        double temperature,
        String condition,
        LocalDate weatherDate,
        LocalDate msgDate
) {}