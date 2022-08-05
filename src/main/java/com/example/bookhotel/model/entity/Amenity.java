package com.example.bookhotel.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "amenities")
@Getter
@Setter
public class Amenity extends BaseEntity{

    @Column(unique = true)
    private String name;
}
