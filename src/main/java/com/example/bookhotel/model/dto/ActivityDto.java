package com.example.bookhotel.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class ActivityDto {
    private Long id;

    private String name;

    private BigDecimal price;

    private String description;

    private Long availableSpots;

    private LocalDate date;

    private String pictureUrl;

    private Set<String> categories;

}
