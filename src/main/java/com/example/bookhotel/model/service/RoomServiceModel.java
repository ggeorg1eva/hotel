package com.example.bookhotel.model.service;

import com.example.bookhotel.model.entity.enumeration.PeopleCountEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class RoomServiceModel {
        private Long id;

        private String name;

        private Double area;

        private PeopleCountEnum numberOfPeople;

        private BigDecimal price;

        private List<String> pictureUrls;

        private Set<String> amenities;
}
