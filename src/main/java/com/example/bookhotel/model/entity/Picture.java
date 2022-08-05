package com.example.bookhotel.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "pictures")
@Getter
@Setter
public class Picture extends BaseEntity {

    @Column(name = "picture_url", nullable = false)
    private String pictureUrl;

}
