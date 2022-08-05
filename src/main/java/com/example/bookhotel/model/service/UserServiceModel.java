package com.example.bookhotel.model.service;

import com.example.bookhotel.model.entity.enumeration.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserServiceModel {
    private String username;

    private String firstName;

    private String lastName;

    private Integer age;

    private String password;

    private String email;

    private Set<UserRoleEnum> roles;
}
