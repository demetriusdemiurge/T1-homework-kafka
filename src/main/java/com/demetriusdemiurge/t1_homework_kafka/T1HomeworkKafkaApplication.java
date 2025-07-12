package com.demetriusdemiurge.t1_homework_kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class T1HomeworkKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(T1HomeworkKafkaApplication.class, args);
	}

}
