package com.example.bookhotel.repository;

import com.example.bookhotel.model.entity.Activity;
import com.example.bookhotel.model.entity.ActivityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAll();
    List<Activity> findAllByDateBefore(LocalDate date);
    Optional<Activity> findByName(String name);

}
