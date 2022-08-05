package com.example.bookhotel.model.service;

import com.example.bookhotel.model.entity.enumeration.PeopleCountEnum;
import com.example.bookhotel.model.entity.enumeration.ReservationStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class RoomReservationServiceModel {

    private LocalDateTime dateOfReservation;

    private LocalDate arrivalDate;

    private LocalDate departureDate;

    private ReservationStatusEnum status;

    private Long roomId;

    private String bookerUsername;
}
