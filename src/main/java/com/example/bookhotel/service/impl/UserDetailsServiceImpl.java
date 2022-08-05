package com.example.bookhotel.service.impl;

import com.example.bookhotel.model.entity.UserRole;
import com.example.bookhotel.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(this::mapUserToUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User with username \"" + username + "\" not found!"));
    }

    private UserDetails mapUserToUserDetails(com.example.bookhotel.model.entity.User user) {

        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(this::mapRoleToAuthority)
                .toList();

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    private GrantedAuthority mapRoleToAuthority(UserRole role) {
        return new SimpleGrantedAuthority("ROLE_" + role.getRole().name());
    }
}
