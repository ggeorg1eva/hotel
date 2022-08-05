package com.example.bookhotel.repository;

import com.example.bookhotel.model.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    @Query("select max(p.id) from Picture p")
    Long getLastId();

    Picture getByPictureUrl(String pictureUrl);


}
