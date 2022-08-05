package com.example.bookhotel.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class RoomReservationDto {

    private LocalDateTime dateOfReservation;

    private LocalDate arrivalDate;

    private LocalDate departureDate;

    private Integer resStat;

    private RoomDto room;

    private String userFullName;
}
