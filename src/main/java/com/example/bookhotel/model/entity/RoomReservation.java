package com.example.bookhotel.model.entity;

import com.example.bookhotel.model.entity.enumeration.ReservationStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_reservations")
@Getter
@Setter
public class RoomReservation {

    @Id
    @Column(name = "placed_On", nullable = false, unique = true)
    private LocalDateTime dateOfReservation;

    @Column(name = "arrival_date", nullable = false)
    private LocalDate arrivalDate;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Enumerated(EnumType.STRING)
    private ReservationStatusEnum status;

    @ManyToOne
    private Room room;

    @ManyToOne
    private User user;

}
