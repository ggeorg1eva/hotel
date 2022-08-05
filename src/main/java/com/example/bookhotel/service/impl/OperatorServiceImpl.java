package com.example.bookhotel.service.impl;

import com.example.bookhotel.model.dto.OperatorDto;
import com.example.bookhotel.model.entity.User;
import com.example.bookhotel.model.entity.UserRole;
import com.example.bookhotel.model.entity.enumeration.UserRoleEnum;
import com.example.bookhotel.repository.UserRepository;
import com.example.bookhotel.repository.UserRoleRepository;
import com.example.bookhotel.service.OperatorService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperatorServiceImpl implements OperatorService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;

    public OperatorServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<OperatorDto> getAllOperators() {
        UserRole adminRole = userRoleRepository.findByRole(UserRoleEnum.ADMIN);

        List<OperatorDto> list = userRepository.
                findAllByRolesRole(UserRoleEnum.OPERATOR).
                stream()
                .filter(user -> !user.getRoles().contains(adminRole))
                .map(user -> modelMapper.map(user, OperatorDto.class))
                .toList();

        return list;
    }

    @Override
    public void removeOperatorRole(Long id) {
        User user = userRepository.findById(id).orElse(null);

        UserRole operatorRole = userRoleRepository.findByRole(UserRoleEnum.OPERATOR);


        if (user != null){
            user.getRoles().remove(operatorRole);
            userRepository.save(user);
        }

    }

}
