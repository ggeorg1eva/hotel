package com.example.bookhotel.repository;

import com.example.bookhotel.model.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAll();
    List<Activity> findAllByDateBefore(LocalDate date);
}
