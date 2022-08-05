package com.example.bookhotel.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "activity_reservations")
@Getter
@Setter
public class ActivityReservation extends BaseEntity {

    @Column(name = "number_of_people", nullable = false)
    private Long peopleCount;

    @ManyToOne
    private User user;

    @ManyToOne
    private Activity activity;

}
