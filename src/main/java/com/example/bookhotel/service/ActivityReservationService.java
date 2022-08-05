package com.example.bookhotel.service;

import com.example.bookhotel.model.dto.ActivityReservationDto;
import com.example.bookhotel.model.entity.Activity;

import java.util.List;

public interface ActivityReservationService {
    void createActivityReservation(Activity activity, String username, Long peopleCount);
    List<ActivityReservationDto> getActivityReservationsByUserId(Long id);

    void deleteAllReservationsByActivity(Activity activity);
}
