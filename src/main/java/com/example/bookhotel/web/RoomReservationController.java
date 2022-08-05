package com.example.bookhotel.web;

import com.example.bookhotel.model.entity.enumeration.ReservationStatusEnum;
import com.example.bookhotel.service.RoomReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@Controller
public class RoomReservationController {
    private final RoomReservationService roomReservationService;

    public RoomReservationController(RoomReservationService roomReservationService) {
        this.roomReservationService = roomReservationService;
    }

    @GetMapping("/rooms/reservations/all")
    public String viewReservations(Model model){
        model.addAttribute("pending", roomReservationService.getAllReservationsByStatus(ReservationStatusEnum.PENDING));
//        model.addAttribute("approved", roomReservationService.getAllReservationsByStatus(ReservationStatusEnum.APPROVED));
//        model.addAttribute("denied", roomReservationService.getAllReservationsByStatus(ReservationStatusEnum.DENIED));

        return "view-reservations";
    }

    @PatchMapping("/rooms/reservations/deny/{id}")
    public String denyReservation(@PathVariable  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                              LocalDateTime id){

        roomReservationService.denyReservation(id); // id is the dateOfReservation

        return "redirect:/rooms/reservations/all";
    }

    @PatchMapping("/rooms/reservations/approve/{id}")
    public String approveReservation(@PathVariable  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                          LocalDateTime id){

        roomReservationService.approveReservation(id); // id is the dateOfReservation

        return "redirect:/rooms/reservations/all";
    }
}
