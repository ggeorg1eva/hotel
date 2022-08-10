package com.example.bookhotel.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "activities")
@Getter
@Setter
public class Activity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "available_spots", nullable = false)
    private Long availableSpots;

    @Column(nullable = false)
    private LocalDate date;

    @OneToOne(cascade = CascadeType.ALL)
    private Picture picture;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<ActivityCategory> categories;
}
