package com.example.bookhotel.model.binding;

import com.example.bookhotel.model.entity.enumeration.PeopleCountEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class RoomAddBindingModel {

    @NotBlank
    @Size(min = 1, max = 20)
    private String name;

    @Positive
    @NotNull
    private Double area;

    private PeopleCountEnum numberOfPeople;

    @Positive
    @NotNull
    private BigDecimal price;

    private Set<String> pictureUrls;

    private Set<String> amenities;
}
