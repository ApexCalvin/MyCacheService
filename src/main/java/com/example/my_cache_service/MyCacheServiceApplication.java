package com.example.my_cache_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MyCacheServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyCacheServiceApplication.class, args);
		System.out.println("Starting caching service...");
		CacheService.dailyUpdateChecker(8); //refresh cache @ 8 AM
	}
}
