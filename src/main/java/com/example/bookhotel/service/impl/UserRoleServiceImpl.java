package com.example.bookhotel.service.impl;

import com.example.bookhotel.model.entity.UserRole;
import com.example.bookhotel.model.entity.enumeration.UserRoleEnum;
import com.example.bookhotel.repository.UserRoleRepository;
import com.example.bookhotel.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }


    @Override
    public void initRoles() {
        if (userRoleRepository.count() == 0) {

            Arrays.stream(UserRoleEnum.values())
                    .forEach(roleEnum -> {
                        UserRole userRole = new UserRole();

                        userRole.setRole(roleEnum);
                        userRoleRepository.save(userRole);
                    });
        }
    }
}
