package com.example.bookhotel.model.dto;

import com.example.bookhotel.model.entity.ActivityReservation;
import com.example.bookhotel.model.entity.enumeration.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserDto {
    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private Integer age;

    private String password;

    private String email;

    private List<String> roles;

}
