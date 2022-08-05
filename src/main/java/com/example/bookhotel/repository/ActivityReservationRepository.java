package com.example.bookhotel.repository;

import com.example.bookhotel.model.entity.Activity;
import com.example.bookhotel.model.entity.ActivityReservation;
import com.example.bookhotel.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityReservationRepository extends JpaRepository<ActivityReservation, Long> {
    List<ActivityReservation> findAllByUser_Id(Long user_id);
    Optional<ActivityReservation> findByActivityAndUser_Username(Activity activity, String user_username);
    List<ActivityReservation> findAllByActivity(Activity activity);
}
