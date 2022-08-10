package com.example.bookhotel.service;

import com.example.bookhotel.model.dto.ActivityDto;
import com.example.bookhotel.model.entity.ActivityCategory;
import com.example.bookhotel.model.service.ActivityServiceModel;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ActivityService {

    void addActivity(ActivityServiceModel map) throws IOException;

    List<ActivityDto> getAllActivities();

    void deleteActivity(Long id) throws IOException;

    ActivityDto getActivityById(Long id);


    void bookActivity(Long id, String username, Long peopleCount);

    int deleteOlderActivities(LocalDate now);

    void removeCategoryFromActivity(ActivityCategory categoryById);
}
