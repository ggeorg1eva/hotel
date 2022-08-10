package com.example.bookhotel.web;


import com.example.bookhotel.model.entity.User;
import com.example.bookhotel.model.entity.enumeration.UserRoleEnum;
import com.example.bookhotel.repository.*;
import com.example.bookhotel.util.TestDataUtils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestDataUtils utils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        utils.initUserRoles();
        utils.createRegularUser();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void testRegistration_Shown() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void testRegistrationPost_Successful() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", "test1")
                        .param("email", "test1@gmail.com")
                        .param("firstName", "Tester")
                        .param("lastName", "Testov")
                        .param("age", "22")
                        .param("password", "1234")
                        .param("confirmPassword", "1234")
                        .param("roles", "OPERATOR")
                        .with(csrf())

                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("login"));

        User registered = userRepository.findByUsername("test1").orElse(null);

        assertEquals("test1@gmail.com", registered.getEmail());
        assertEquals("Tester", registered.getFirstName());
        assertEquals("Testov", registered.getLastName());
        assertEquals(22, registered.getAge());
        assertTrue(this.passwordEncoder.matches("1234", registered.getPassword()));

    }

    @Test
    public void testRegistrationPost_WithIncorrectData_RedirectToRegister() throws Exception {
        BindingResult bindingResult = (BindingResult) mockMvc
                .perform(
                        post("/users/register")
                                .param("username", "")
                                .param("email", "")
                                .param("firstName", "")
                                .param("lastName", "")
                                .param("age", "-9")
                                .param("password", "1")
                                .param("confirmPassword", "1")
                                .param("roles", "")
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("register"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.userRegisterBindingModel"))
                .andReturn()
                .getFlashMap()
                .get("org.springframework.validation.BindingResult.userRegisterBindingModel");

        assertTrue(bindingResult.hasFieldErrors("username"));
        assertTrue(bindingResult.hasFieldErrors("email"));
        assertTrue(bindingResult.hasFieldErrors("firstName"));
        assertTrue(bindingResult.hasFieldErrors("lastName"));
        assertTrue(bindingResult.hasFieldErrors("age"));
        assertTrue(bindingResult.hasFieldErrors("password"));
        assertTrue(bindingResult.hasFieldErrors("confirmPassword"));
    }

    @Test
    public void testRegisterPost_WithExistingUserData_RedirectToRegister() throws Exception {
        User user = new User();

        user.setUsername("test1");
        user.setFirstName("Tester");
        user.setLastName("Testov");
        user.setAge(22);
        user.setPassword(passwordEncoder.encode("1234"));
        user.setEmail("test1@gmail.com");
        user.setRoles(Set.of(userRoleRepository.findByRole(UserRoleEnum.USER)));

        userRepository.save(user);

       boolean isUserExist = (boolean) mockMvc.perform(post("/users/register")
                       .param("username", "test1")
                       .param("email", "test1@gmail.com")
                       .param("firstName", "Tester")
                       .param("lastName", "Testov")
                       .param("age", "22")
                       .param("password", "1234")
                       .param("confirmPassword", "1234")
                       .param("roles", "USER")
                       .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("register"))
                .andExpect(flash().attributeExists("userExist"))
                .andReturn()
                .getFlashMap()
                .get("userExist");

       assertTrue(isUserExist);
    }

    @Test
    @WithMockUser(username = "test-user")
    public void testUserProfileShown_WithRegisteredUser() throws Exception {
        mockMvc.perform(get("/users/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-profile"));
    }

    @Test
    public void testUserProfileRedirectToLogin_WithUnregisteredUser() throws Exception {
        mockMvc.perform(get("/users/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }
}
