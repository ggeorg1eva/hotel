package com.example.bookhotel.web;

import com.example.bookhotel.model.entity.Activity;
import com.example.bookhotel.model.entity.ActivityReservation;
import com.example.bookhotel.model.entity.User;
import com.example.bookhotel.repository.ActivityRepository;
import com.example.bookhotel.repository.ActivityReservationRepository;
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
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ActivityControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils utils;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityReservationRepository activityReservationRepository;

    private Activity testActivity;
    private ActivityReservation activityReservation;
    private User testUser;

    @BeforeEach
    public void setUp() {
        utils.initCategories();
        testUser = utils.createRegularUser();
        utils.createOperator();
        testActivity = utils.createActivityWithCategoryRelax();
//        activityReservation = utils.createActivityReservation(testActivity, testUser);
    }

    @AfterEach
    public void tearDown() {
//        activityReservationRepository.deleteAll();
        activityRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testAddActivityGet_WithUnregisteredUser_RedirectToLogin() throws Exception {
        mockMvc.perform(get("/activities/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "test-user")
    public void testAddActivityGet_WithRegisteredUser_NotOperator_Forbidden() throws Exception {
        mockMvc.perform(get("/activities/add"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testAddActivityGet_WithRegisteredUser_Operator_Shown() throws Exception {
        mockMvc.perform(get("/activities/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-activity"));

    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testAddActivityPost_WithCorrectData_Successful() throws Exception {
        mockMvc.perform(post("/activities/add")
                        .param("name", "activityTest")
                        .param("price", "22.50")
                        .param("description", "blablabla")
                        .param("availableSpots", "4")
                        .param("date", "2022-09-25")
                        .param("categories", "Romantic")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("all"));

        Activity saved = activityRepository.findByName("activityTest").orElse(null);

        assertEquals("activityTest", saved.getName());
        assertEquals("blablabla", saved.getDescription());
        assertEquals(4L, saved.getAvailableSpots());
        assertEquals(LocalDate.of(2022, 9, 25), saved.getDate());
        assertEquals(1, saved.getCategories().size());
    }


    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testAddActivityPost_WithIncorrectData_RedirectToAdd() throws Exception {
        BindingResult bindingResult = (BindingResult) mockMvc.perform(post("/activities/add")
                        .param("name", "")
                        .param("description", "")
                        .param("availableSpots", "-9")
                        .param("date", "2022-07-25")
                        .param("categories", "Romantic")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("add"))
                .andReturn()
                .getFlashMap()
                .get("org.springframework.validation.BindingResult.activityAddBindingModel");

        assertTrue(bindingResult.hasFieldErrors("name"));
        assertTrue(bindingResult.hasFieldErrors("description"));
        assertTrue(bindingResult.hasFieldErrors("availableSpots"));
        assertTrue(bindingResult.hasFieldErrors("date"));
    }

    @Test
    public void testAllActivities_Shown() throws Exception {
        mockMvc.perform(get("/activities/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("all-activities"));
    }


    @Test
    public void testDeleteActivity_WithUnregisteredUser_RedirectToLogin() throws Exception {
        Long id = testActivity.getId();
        mockMvc.perform(delete("/activities/delete/{id}", id)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "test-user", roles = "USER")
    public void testDeleteActivity_WithRegisteredUser_NotOperator_Forbidden() throws Exception {
        Long id = testActivity.getId();
        mockMvc.perform(delete("/activities/delete/{id}", id)
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testDeleteActivity_WithRegisteredUser_Operator_Successful() throws Exception {
        Long id = testActivity.getId();
        mockMvc.perform(delete("/activities/delete/{id}", id)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/activities/all"));

    }

    @Test
    public void testBookActivityGet_WithUnregisteredUser_RedirectToLogin() throws Exception {
        mockMvc.perform(get("/activities/book/{id}", testActivity.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "test-user", roles = "USER")
    public void testBookActivityGet_WithRegisteredUser_Shown() throws Exception {
        mockMvc.perform(get("/activities/book/{id}", testActivity.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test-user")
    public void testBookActivityPatch_WithRegisteredUser_WithNegativePeopleCount_Unsuccessful() throws Exception {
        BindingResult bindingResult = (BindingResult) mockMvc
                .perform(patch("/activities/book/{id}", testActivity.getId())
                        .param("peopleCount", "-7")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/activities/book/?"))
                .andReturn()
                .getFlashMap()
                .get("org.springframework.validation.BindingResult.activityBookBindingModel");

        assertTrue(bindingResult.hasFieldErrors("peopleCount"));
    }

    @Test
    @WithMockUser(username = "test-user")
    public void testBookActivityPatch_WithRegisteredUser_WithBiggerThanAvailableSpotsPeopleCount_Unsuccessful() throws Exception {
        Boolean invalidPeopleCount = (Boolean) mockMvc
                .perform(patch("/activities/book/{id}", testActivity.getId())
                        .param("peopleCount", "20")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/activities/book/?"))
                .andReturn()
                .getFlashMap()
                .get("invalidPeopleCount");

        assertTrue(invalidPeopleCount);
    }

    @Test
    @WithMockUser(username = "test-user", roles = "USER")
    public void testBookActivityPatch_WithRegisteredUser_WithCorrectData_Successful() throws Exception {
        activityReservation = utils.createActivityReservation(testActivity, testUser);
        mockMvc.perform(patch("/activities/book/{id}", testActivity.getId())
                        .param("peopleCount", "2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"));

        activityReservationRepository.deleteAll();
    }
}
