package com.example.bookhotel.repository;

import com.example.bookhotel.model.entity.Room;
import com.example.bookhotel.model.entity.RoomReservation;
import com.example.bookhotel.model.entity.enumeration.ReservationStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long> {
    List<RoomReservation> findAllByUser_Id(Long user_id);
    List<RoomReservation> findAllByStatus(ReservationStatusEnum status);
    Optional<RoomReservation> findByDateOfReservation(LocalDateTime dateOfReservation);
    List<RoomReservation> findAllByRoom(Room room);
}
