package com.example.bookhotel.service;

import com.example.bookhotel.model.entity.User;
import com.example.bookhotel.model.entity.UserRole;
import com.example.bookhotel.model.entity.enumeration.UserRoleEnum;
import com.example.bookhotel.repository.UserRepository;
import com.example.bookhotel.service.impl.UserDetailsServiceImpl;
import com.example.bookhotel.util.TestDataUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

    @Mock
    private UserRepository mockUserRepo;

    private UserDetailsService userDetailsService;

    @BeforeEach
    public void setUp() {
        this.userDetailsService = new UserDetailsServiceImpl(mockUserRepo);
    }

    @Test
    public void testLoadUserByUsername_WithNotExistingUsername_ThrowsException() {
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("nonexistent"));
    }

    @Test
    public void testLoadUserByUsername_WithExistingUsername_Successful() {
        UserRole userRole = new UserRole();
        userRole.setRole(UserRoleEnum.USER);

        User testUser = new User();
        testUser.setUsername("test-user");
        testUser.setFirstName("User");
        testUser.setLastName("Userov");
        testUser.setAge(25);
        testUser.setPassword("1234");
        testUser.setEmail("tuser@gmail.com");
        testUser.setRoles(Set.of(userRole));

        when(mockUserRepo.findByUsername(testUser.getUsername()))
                .thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername(testUser.getUsername());

        assertEquals(testUser.getUsername(), userDetails.getUsername());
        assertEquals(testUser.getPassword(), userDetails.getPassword());

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        assertEquals(1, authorities.size());

        assertEquals("ROLE_" + UserRoleEnum.USER.name(),
                authorities.iterator().next().getAuthority());
    }
}
