package com.example.bookhotel.service;

import com.example.bookhotel.model.dto.OperatorDto;
import com.example.bookhotel.model.dto.UserDto;
import com.example.bookhotel.model.entity.User;
import com.example.bookhotel.model.service.UserServiceModel;

import java.util.List;

public interface UserService {
    void initUsers();
    void registerUser(UserServiceModel model);
    boolean isUserExist(String username, String email);
    User findUserByUsername(String username);
    UserDto getDtoByUsername(String username);
}
