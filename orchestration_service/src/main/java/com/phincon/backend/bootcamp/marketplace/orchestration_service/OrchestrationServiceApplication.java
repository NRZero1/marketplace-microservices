package com.phincon.backend.bootcamp.marketplace.orchestration_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class OrchestrationServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrchestrationServiceApplication.class, args);
	}

}
