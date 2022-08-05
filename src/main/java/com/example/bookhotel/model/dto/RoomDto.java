package com.example.bookhotel.model.dto;

import com.example.bookhotel.model.entity.enumeration.PeopleCountEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class RoomDto {
    private Long id;

    private String name;

    private Double area;

    private PeopleCountEnum numberOfPeople;

    private BigDecimal price;

    private String firstPicUrl; // for 'all rooms' view

    private List<String> pictureUrls;

    private Set<String> amenities;
}
