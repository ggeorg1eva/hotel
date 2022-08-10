package com.example.bookhotel.model.entity;

import com.example.bookhotel.model.entity.enumeration.PeopleCountEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "rooms")
@Getter
@Setter
public class Room extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double area;

    @Enumerated(EnumType.STRING)
    private PeopleCountEnum numberOfPeople;

    @Column(nullable = false)
    private BigDecimal price;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Picture> pictures;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Amenity> amenities;
}
