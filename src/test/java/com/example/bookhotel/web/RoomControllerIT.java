package com.example.bookhotel.web;

import com.example.bookhotel.model.entity.Activity;
import com.example.bookhotel.model.entity.ActivityReservation;
import com.example.bookhotel.model.entity.Room;
import com.example.bookhotel.model.entity.User;
import com.example.bookhotel.model.entity.enumeration.PeopleCountEnum;
import com.example.bookhotel.repository.*;
import com.example.bookhotel.util.TestDataUtils;
import lombok.With;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils utils;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private RoomReservationRepository roomReservationRepository;

    private User testUser;

    private Room testRoom;

    @BeforeEach
    public void setUp() {
        testUser = utils.createRegularUser();
        utils.createOperator();
        testRoom = utils.createRoom();
    }

    @AfterEach
    public void tearDown() {
        roomReservationRepository.deleteAll();
        userRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    public void testAddRoomGet_withUnregisteredUser_RedirectToLogin() throws Exception {
        mockMvc.perform(get("/rooms/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "test-user")
    public void testAddRoomGet_withRegisteredUser_NotOperator_Forbidden() throws Exception {
        mockMvc.perform(get("/rooms/add"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testAddRoomGet_withRegisteredUser_Operator_Shown() throws Exception {
        mockMvc.perform(get("/rooms/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-room"));

    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testAddRoomPost_WithIncorrectData_RedirectToAdd() throws Exception {
        BindingResult bindingResult = (BindingResult)
                mockMvc.perform(post("/rooms/add")
                                .param("name", "")
                                .param("area", "")
                                .param("price", "-12")
                                .param("amenities", "Wifi")
                                .with(csrf())
                        )
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("add"))
                        .andReturn()
                        .getFlashMap()
                        .get("org.springframework.validation.BindingResult.roomAddBindingModel");

        assertTrue(bindingResult.hasFieldErrors("name"));
        assertTrue(bindingResult.hasFieldErrors("area"));
        assertTrue(bindingResult.hasFieldErrors("price"));
    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testAddRoomPost_WithCorrectData_Successful() throws Exception {
        mockMvc.perform(post("/rooms/add")
                        .param("name", "RoomTest")
                        .param("area", "12.3")
                        .param("price", "10")
                        .param("amenities", "Balcony")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("all"));

        Room saved = roomRepository.findByName("RoomTest").orElse(null);

        assertEquals("RoomTest", saved.getName());
        assertEquals(12.3, saved.getArea());
        assertEquals(BigDecimal.valueOf(10).doubleValue(), saved.getPrice().doubleValue());
        assertEquals(1, saved.getAmenities().size());
    }

    @Test
    public void testAllRooms_Shown() throws Exception {
        mockMvc.perform(get("/rooms/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("all-rooms"));
    }

    @Test
    public void testViewRoom_Shown() throws Exception {
        mockMvc.perform(get("/rooms/{id}", testRoom.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("view-room"));
    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testDeleteRoom_WithRegisteredUser_Operator_Successful() throws Exception {
        mockMvc.perform(delete("/rooms/{id}/delete", testRoom.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rooms/all"));

        assertEquals(0, roomRepository.count());
    }

    @Test
    public void testBookRoomGet_UnregisteredUser_RedirectToLogin() throws Exception {
        mockMvc.perform(get("/rooms/{id}/reserve", testRoom.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "test-user")
    public void testBookRoomGet_RegisteredUser_Shown() throws Exception {
        mockMvc.perform(get("/rooms/{id}/reserve", testRoom.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("book-room"));


    }

    @Test
    @WithMockUser(username = "test-user")
    public void testBookRoomPost_RegisteredUser_WithIncorrectData_RedirectToReserve() throws Exception {
        BindingResult bindingResult = (BindingResult) mockMvc.perform(post("/rooms/{id}/reserve", testRoom.getId())
                        .param("arrivalDate", "2022-05-04")
                        .param("departureDate", "2022-05-08")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/rooms/?/reserve"))
                .andReturn()
                .getFlashMap()
                .get("org.springframework.validation.BindingResult.roomBookBindingModel");

        assertTrue(bindingResult.hasFieldErrors("arrivalDate"));
        assertTrue(bindingResult.hasFieldErrors("departureDate"));
    }

    @Test
    @WithMockUser(username = "test-user")
    public void testBookRoomPost_RegisteredUser_WithCorrectData_Successful() throws Exception {
                mockMvc.perform(post("/rooms/{id}/reserve", testRoom.getId())
                .param("arrivalDate", "2022-10-04")
                .param("departureDate", "2022-10-08")
                .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"));

                assertEquals(1, roomRepository.count());
    }

}
