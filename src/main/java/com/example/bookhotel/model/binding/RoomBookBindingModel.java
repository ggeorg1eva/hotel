package com.example.bookhotel.model.binding;

import com.example.bookhotel.model.entity.enumeration.PeopleCountEnum;
import com.example.bookhotel.model.entity.enumeration.ReservationStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.LongAccumulator;

@Getter
@Setter
public class RoomBookBindingModel {

    private LocalDateTime dateOfReservation;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future
    private LocalDate arrivalDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future
    private LocalDate departureDate;

    private ReservationStatusEnum status;

    private Long roomId;

    private String bookerUsername;

}
