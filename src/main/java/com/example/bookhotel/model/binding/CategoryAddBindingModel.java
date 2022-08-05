package com.example.bookhotel.model.binding;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CategoryAddBindingModel {

    @NotBlank
    @Size(min = 3, max = 20)
    private String name;
}
