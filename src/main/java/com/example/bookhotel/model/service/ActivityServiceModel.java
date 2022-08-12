package com.example.bookhotel.model.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class ActivityServiceModel {

    private String name;

    private String description;

    private BigDecimal price;

    private Long availableSpots;

    private LocalDate date;

    private MultipartFile picture;

    private Set<String> categories;
}
