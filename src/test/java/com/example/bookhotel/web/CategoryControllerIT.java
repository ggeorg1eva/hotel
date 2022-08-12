package com.example.bookhotel.web;

import com.example.bookhotel.model.entity.Activity;
import com.example.bookhotel.model.entity.ActivityCategory;
import com.example.bookhotel.model.entity.ActivityReservation;
import com.example.bookhotel.model.entity.User;
import com.example.bookhotel.repository.ActivityRepository;
import com.example.bookhotel.repository.ActivityReservationRepository;
import com.example.bookhotel.repository.CategoryRepository;
import com.example.bookhotel.repository.UserRepository;
import com.example.bookhotel.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils utils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @BeforeEach
    public void setUp() {
        utils.initCategories();
        utils.createRegularUser();
        utils.createOperator();

    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        activityRepository.deleteAll();
        categoryRepository.deleteAll();
    }


    @Test
    public void testAllCats_WithUnregisteredUser_RedirectToLogin() throws Exception {
        mockMvc.perform(get("/categories/all"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "test-user")
    public void testAllCats_WithRegisteredUser_NotOperator_Forbidden() throws Exception {
        mockMvc.perform(get("/categories/all"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testAllCats_WithRegisteredUser_Operator_Shown() throws Exception {
        mockMvc.perform(get("/categories/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("view-categories"));
    }

    @Test
    public void testAddCategoryGet_WithUnregisteredUser_RedirectToLogin() throws Exception {
        mockMvc.perform(get("/categories/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "test-user")
    public void testAddCategoryGet_WithRegisteredUser_NotOperator_Forbidden() throws Exception {
        mockMvc.perform(get("/categories/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testAddCategoryGet_WithRegisteredUser_Operator_Shown() throws Exception {
        mockMvc.perform(get("/categories/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-category"));
    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testAddCategoryPost_WithRegisteredUser_Operator_WithIncorrectData_RedirectToAdd() throws Exception {
        BindingResult bindingResult = (BindingResult) mockMvc.perform(post("/categories/add")
                        .param("name", "")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("add"))
                .andReturn()
                .getFlashMap()
                .get("org.springframework.validation.BindingResult.categoryAddBindingModel");

        assertTrue(bindingResult.hasFieldErrors("name"));
    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testAddCategoryPost_WithRegisteredUser_Operator_WithCatNameAlreadyExistent_RedirectToAdd() throws Exception {
        ActivityCategory category = new ActivityCategory();
        category.setName("cat1");
        categoryRepository.save(category);

        mockMvc.perform(post("/categories/add")
                        .param("name", "cat1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("add"));
    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testAddCategoryPost_WithRegisteredUser_Operator_WithCorrectData_Successful() throws Exception {
        long countBeforeRequest = categoryRepository.count();

        mockMvc.perform(post("/categories/add")
                        .param("name", "testCat1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("all"));

        assertEquals(countBeforeRequest + 1, categoryRepository.count());
    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testDeleteCategory_WithoutActivitiesInThisCat_Successful() throws Exception {
        ActivityCategory category = new ActivityCategory();
        category.setName("testCat1");
        categoryRepository.save(category);

        mockMvc.perform(delete("/categories/delete/{id}", category.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories/all"));

        Optional<ActivityCategory> deletedCat = categoryRepository.findByName("testCat1");

        assertTrue(deletedCat.isEmpty());
    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testDeleteCategory_WithActivityInThisCat_Successful() throws Exception {
        ActivityCategory category = new ActivityCategory();
        category.setName("testCat1");
        categoryRepository.save(category);

        Activity activity = utils.createActivityWithGivenCategory(category);


        mockMvc.perform(delete("/categories/delete/{id}", category.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories/all"));

        //when only one category for this activity -> it should be deleted with the category
        assertTrue(activityRepository.findByName("activity test2").isEmpty());
    }
}
