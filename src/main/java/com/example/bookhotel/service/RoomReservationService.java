package com.example.bookhotel.service;

import com.example.bookhotel.model.dto.RoomReservationDto;
import com.example.bookhotel.model.entity.Room;
import com.example.bookhotel.model.entity.enumeration.ReservationStatusEnum;
import com.example.bookhotel.model.service.RoomReservationServiceModel;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomReservationService {
    void createReservation(RoomReservationServiceModel serviceModel, Room room);
    List<RoomReservationDto> getRoomReservationsByUserid(Long id);

    List<RoomReservationDto> getAllReservationsByStatus(ReservationStatusEnum status);

    void denyReservation(LocalDateTime dateOfReservation);

    void approveReservation(LocalDateTime dateOfReservation);

    void deleteAllReservationsByRoom(Room room);
}
