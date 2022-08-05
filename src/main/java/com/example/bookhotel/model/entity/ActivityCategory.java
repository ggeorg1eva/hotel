package com.example.bookhotel.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "activity_categories")
@Getter
@Setter
public class ActivityCategory extends BaseEntity {

    @Column(unique = true)
    String name;
}
