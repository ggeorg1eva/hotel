package com.example.bookhotel.model.binding;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class ActivityBookBindingModel {

    @NotNull
    @Positive
    private Long peopleCount;
}
