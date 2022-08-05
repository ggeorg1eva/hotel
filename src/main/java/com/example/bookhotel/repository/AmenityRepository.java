package com.example.bookhotel.repository;

import com.example.bookhotel.model.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    Amenity findByName(String name);
}
