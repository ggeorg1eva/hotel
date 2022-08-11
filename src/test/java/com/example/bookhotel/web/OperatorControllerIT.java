package com.example.bookhotel.web;

import com.example.bookhotel.model.entity.User;
import com.example.bookhotel.model.entity.UserRole;
import com.example.bookhotel.repository.UserRepository;
import com.example.bookhotel.repository.UserRoleRepository;
import com.example.bookhotel.service.OperatorService;
import com.example.bookhotel.util.TestDataUtils;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OperatorControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private TestDataUtils utils;

    private User testOperator;

    @BeforeEach
    public void setUp(){
        testOperator = utils.createOperator();
        utils.createAdmin();
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    public void testRemoveOperatorRole_WithUnregisteredUser_RedirectToLogin() throws Exception {
        mockMvc.perform(get("/operators/remove-role/{id}", testOperator.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "test-operator")
    public void testRemoveOperatorRole_WithRegisteredUser_NotAdmin_Forbidden() throws Exception {
        mockMvc.perform(get("/operators/remove-role/{id}", testOperator.getId()))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = "test-admin", roles = "ADMIN")
    public void testRemoveOperatorRole_WithRegisteredUser_Admin_Successful() throws Exception {
        mockMvc.perform(patch("/operators/remove-role/{id}", testOperator.getId())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/operators"));

        User user = userRepository.findByUsername("test-operator").orElse(null);

        assertEquals(0, user.getRoles().size());
    }
}
