package com.example.bookhotel.model.binding;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;



@Getter
@Setter
public class PictureAddBindingModel {

    @NotNull
    private MultipartFile picture;
}
