package com.example.bookhotel.web;

import com.example.bookhotel.model.entity.Activity;
import com.example.bookhotel.model.entity.Room;
import com.example.bookhotel.model.entity.RoomReservation;
import com.example.bookhotel.model.entity.User;
import com.example.bookhotel.model.entity.enumeration.ReservationStatusEnum;
import com.example.bookhotel.repository.RoomRepository;
import com.example.bookhotel.repository.RoomReservationRepository;
import com.example.bookhotel.repository.UserRepository;
import com.example.bookhotel.util.TestDataUtils;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomReservationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomReservationRepository roomReservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TestDataUtils utils;

    private User testUser;
    private Room testRoom;

    @BeforeEach
    public void setUp() {
        testUser = utils.createRegularUser();
        testRoom = utils.createRoom();
        utils.createOperator();
        utils.createRoomReservation(testRoom, testUser);
    }

    @AfterEach
    public void tearDown() {
        roomReservationRepository.deleteAll();
        userRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    public void testViewReservations_withUnregisteredUser_RedirectToLogin() throws Exception {
        mockMvc.perform(get("/rooms/reservations/all"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "test-user")
    public void testViewReservations_withRegisteredUser_NotOperator_Forbidden() throws Exception {
        mockMvc.perform(get("/rooms/reservations/all"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testViewReservations_withRegisteredUser_Operator_Shown() throws Exception {
        mockMvc.perform(get("/rooms/reservations/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("view-reservations"));

    }

    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testDenyReservationPatch_Successful() throws Exception {
        mockMvc.perform(patch("/rooms/reservations/deny/{id}",
                        roomReservationRepository.findAll().get(0).getDateOfReservation())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rooms/reservations/all"));

        RoomReservation denied = roomReservationRepository.findAll().get(0);

        assertEquals(ReservationStatusEnum.DENIED, denied.getStatus());
    }


    @Test
    @WithMockUser(username = "test-operator", roles = "OPERATOR")
    public void testApproveReservationPatch_Successful() throws Exception {
        mockMvc.perform(patch("/rooms/reservations/approve/{id}",
                        roomReservationRepository.findAll().get(0).getDateOfReservation())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rooms/reservations/all"));

        RoomReservation approved = roomReservationRepository.findAll().get(0);

        assertEquals(ReservationStatusEnum.APPROVED, approved.getStatus());
    }
}
