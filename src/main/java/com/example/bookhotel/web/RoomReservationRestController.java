package com.example.bookhotel.web;

import com.example.bookhotel.model.dto.RoomReservationDto;
import com.example.bookhotel.model.entity.enumeration.ReservationStatusEnum;
import com.example.bookhotel.service.RoomReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoomReservationRestController {
    private final RoomReservationService roomReservationService;

    public RoomReservationRestController(RoomReservationService roomReservationService) {
        this.roomReservationService = roomReservationService;
    }

    @GetMapping("/rooms/reservations/approved")
    public ResponseEntity<List<RoomReservationDto>> getAllApprovedReservations(){
        List<RoomReservationDto> approved = roomReservationService.getAllReservationsByStatus(ReservationStatusEnum.APPROVED);

        return ResponseEntity.ok(approved);
    }
    @GetMapping("/rooms/reservations/denied")
    public ResponseEntity<List<RoomReservationDto>> getAllDeniedReservations(){
        List<RoomReservationDto> approved = roomReservationService.getAllReservationsByStatus(ReservationStatusEnum.DENIED);

        return ResponseEntity.ok(approved);
    }
}
