package com.example.bookhotel.web;

import com.example.bookhotel.repository.UserRepository;
import com.example.bookhotel.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils utils;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        utils.createAdmin();
        utils.createRegularUser();
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    public void testGetAllOperators_withUnregisteredUser_RedirectToLogin() throws Exception {
        mockMvc.perform(get("/admin/operators"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "test-user")
    public void testGetAllOperators_WithRegisteredUser_NotAdmin_Forbidden() throws Exception {
        mockMvc.perform(get("/admin/operators"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test-admin", roles = "ADMIN")
    public void testGetAllOperators_WithRegisteredUser_Admin_Shown() throws Exception {
        mockMvc.perform(get("/admin/operators"))
                .andExpect(status().isOk())
                .andExpect(view().name("view-operators"));
    }
}
