package com.example.bookhotel.service.impl;

import com.example.bookhotel.service.ActivityService;
import com.example.bookhotel.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class ScheduleServiceImpl implements ScheduleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceImpl.class);
    private final ActivityService activityService;

    public ScheduleServiceImpl(ActivityService activityService) {
        this.activityService = activityService;
    }

        @Scheduled(cron = "0 0 0 * * *")
    public void deleteActivitiesWithOlderDatesThanNow() {
        LocalDate now = LocalDate.now();

        int count = activityService.deleteOlderActivities(now);

        LOGGER.info("{} activities were deleted today", count);
    }

}