package com.example.bookhotel.model.binding;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class ActivityAddBindingModel {

    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    @Size(min = 5)
    private String description;

    @NotNull
    @Positive
    private Long availableSpots;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future
    private LocalDate date;

    @NotEmpty
    private Set<String> categories;

    private MultipartFile picture;
}
