package com.example.bookhotel.repository;

import com.example.bookhotel.model.entity.ActivityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<ActivityCategory, Long> {
    Optional<ActivityCategory> findByName(String name);
}
