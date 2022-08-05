package com.example.bookhotel.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ActivityReservationDto {

    private Long peopleCount;

    private String activityName;

    private LocalDate activityDate;
}
