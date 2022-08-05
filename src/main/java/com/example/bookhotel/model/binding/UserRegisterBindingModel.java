package com.example.bookhotel.model.binding;

import com.example.bookhotel.model.entity.enumeration.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
public class UserRegisterBindingModel {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 3, max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 20)
    private String lastName;

    @NotNull
    @Min(18)
    private Integer age;

    @NotBlank
    @Size(min = 3, max = 20)
    private String password;

    @NotBlank
    @Size(min = 3, max = 20)
    private String confirmPassword;

    @NotBlank
    @Email
    private String email;

    private Set<UserRoleEnum> roles;
}
