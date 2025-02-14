package com.example.my_cache_service.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScheduleService {
    public static void dailyUpdateChecker(int refreshHour) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if(now.getHour() == refreshHour) {
            CacheService.refreshCache();
            System.out.println("Cache has been refreshed @ " + now.format(formatter));
        }
    }
}
