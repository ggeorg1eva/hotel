package com.example.bookhotel.service.impl;

import com.example.bookhotel.model.dto.UserDto;
import com.example.bookhotel.model.entity.User;
import com.example.bookhotel.model.entity.UserRole;
import com.example.bookhotel.model.entity.enumeration.UserRoleEnum;
import com.example.bookhotel.model.service.UserServiceModel;
import com.example.bookhotel.repository.UserRepository;
import com.example.bookhotel.repository.UserRoleRepository;
import com.example.bookhotel.service.ActivityReservationService;
import com.example.bookhotel.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }


    @Override
    public void initUsers() {
        if (userRepository.count() == 0) {
            initAdmin();
            initRegularUser();
            initOperator();
        }
    }

    @Override
    public void registerUser(UserServiceModel model) {
        Set<UserRole> roles = model.getRoles().stream()
                .map(userRoleRepository::findByRole)
                .collect(Collectors.toSet());

        User user = modelMapper.map(model, User.class);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public boolean isUserExist(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email).isPresent();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public UserDto getDtoByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);

        UserDto userDto = modelMapper.map(user, UserDto.class);

        if (user.getRoles().isEmpty()) {
            userDto.setRoles(List.of("GUEST"));
        } else {
            userDto.setRoles(user.getRoles()
                    .stream()
                    .map(role -> role.getRole().name())
                    .collect(Collectors.toList()));
        }
        return userDto;
    }

    private void initRegularUser() {
        User user = new User();
        user.setUsername("user");
        user.setFirstName("User");
        user.setLastName("Userov");
        user.setAge(25);
        user.setPassword(passwordEncoder.encode("1234"));
        user.setEmail("user@gmail.com");
        user.setRoles(Set.of(userRoleRepository.findByRole(UserRoleEnum.USER)));

        userRepository.save(user);
    }

    private void initOperator() {
        User user = new User();
        user.setUsername("operator");
        user.setFirstName("Operator");
        user.setLastName("Operatorov");
        user.setAge(35);
        user.setPassword(passwordEncoder.encode("1234"));
        user.setEmail("operator@gmail.com");
        user.setRoles(Set.of(userRoleRepository.findByRole(UserRoleEnum.OPERATOR)));

        userRepository.save(user);
    }

    private void initAdmin() {
        User user = new User();
        user.setUsername("admin");
        user.setFirstName("Admin");
        user.setLastName("Adminov");
        user.setAge(39);
        user.setPassword(passwordEncoder.encode("1234"));
        user.setEmail("admin@gmail.com");
        user.setRoles(Set.of(userRoleRepository.findByRole(UserRoleEnum.OPERATOR),
                userRoleRepository.findByRole(UserRoleEnum.ADMIN)));

        userRepository.save(user);
    }
}
