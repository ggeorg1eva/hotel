package com.example.bookhotel.util;

import com.example.bookhotel.model.entity.*;
import com.example.bookhotel.model.entity.enumeration.PeopleCountEnum;
import com.example.bookhotel.model.entity.enumeration.ReservationStatusEnum;
import com.example.bookhotel.model.entity.enumeration.UserRoleEnum;
import com.example.bookhotel.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class TestDataUtils {
    private final ActivityRepository activityRepository;
    private final AmenityRepository amenityRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoomRepository roomRepository;
    private final RoomReservationRepository roomReservationRepository;
    private final ActivityReservationRepository activityReservationRepository;
    private final PasswordEncoder passwordEncoder;

    public TestDataUtils(ActivityRepository activityRepository, AmenityRepository amenityRepository, CategoryRepository categoryRepository, UserRepository userRepository, UserRoleRepository userRoleRepository, RoomRepository roomRepository, RoomReservationRepository roomReservationRepository, ActivityReservationRepository activityReservationRepository, PasswordEncoder passwordEncoder) {
        this.activityRepository = activityRepository;
        this.amenityRepository = amenityRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roomRepository = roomRepository;
        this.roomReservationRepository = roomReservationRepository;
        this.activityReservationRepository = activityReservationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void initUserRoles() {
        if (userRoleRepository.count() == 0) {
            UserRole userRole = new UserRole();
            userRole.setRole(UserRoleEnum.USER);

            UserRole operatorRole = new UserRole();
            userRole.setRole(UserRoleEnum.OPERATOR);

            UserRole adminRole = new UserRole();
            userRole.setRole(UserRoleEnum.ADMIN);

            userRoleRepository.save(userRole);
            userRoleRepository.save(operatorRole);
            userRoleRepository.save(adminRole);
        }
    }

    public User createRegularUser() {
        this.initUserRoles();
        User user = new User();
        user.setUsername("test-user");
        user.setFirstName("User");
        user.setLastName("Userov");
        user.setAge(25);
        user.setPassword(passwordEncoder.encode("1234"));
        user.setEmail("tuser@gmail.com");
        user.setRoles(Set.of(userRoleRepository.findByRole(UserRoleEnum.USER)));

        return userRepository.save(user);

    }

    public void createOperator() {
        this.initUserRoles();
        User user = new User();
        user.setUsername("test-operator");
        user.setFirstName("Operator");
        user.setLastName("Operatorov");
        user.setAge(35);
        user.setPassword(passwordEncoder.encode("1234"));
        user.setEmail("toperator@gmail.com");
        user.setRoles(Set.of(userRoleRepository.findByRole(UserRoleEnum.OPERATOR)));

        userRepository.save(user);
    }

    public void createAdmin() {
        this.initUserRoles();
        User user = new User();
        user.setUsername("test-admin");
        user.setFirstName("Admin");
        user.setLastName("Adminov");
        user.setAge(39);
        user.setPassword(passwordEncoder.encode("1234"));
        user.setEmail("tadmin@gmail.com");
        user.setRoles(Set.of(userRoleRepository.findByRole(UserRoleEnum.OPERATOR),
                userRoleRepository.findByRole(UserRoleEnum.ADMIN)));

        userRepository.save(user);
    }

    public void initCategories() {
        if (categoryRepository.count() == 0) {
            List<String> cats = List.of("Relax",
                    "Entertainment",
                    "Nature",
                    "Romantic",
                    "Kids");

            cats
                    .forEach(name -> {
                        ActivityCategory activityCategory = new ActivityCategory();
                        activityCategory.setName(name);
                        categoryRepository.save(activityCategory);
                    });
        }
    }

    public Activity createActivityWithCategoryRelax() {
        initCategories();
        Activity activity = new Activity();
        activity.setAvailableSpots(3L);
        activity.setName("activity test");
        activity.setDate(LocalDate.of(2022, 9, 9));
        activity.setDescription("test");
        activity.setCategories(Set.of(Objects.requireNonNull(categoryRepository.findByName("Relax").orElse(null))));

        return activityRepository.save(activity);
    }

    public Activity createActivityWithGivenCategory(ActivityCategory category) {
        initCategories();
        Activity activity = new Activity();
        activity.setAvailableSpots(3L);
        activity.setName("activity test2");
        activity.setDate(LocalDate.of(2022, 9, 9));
        activity.setDescription("test");
        activity.setCategories(Set.of(category));

        return activityRepository.save(activity);
    }

    public ActivityReservation createActivityReservation(Activity activity, User user) {
        ActivityReservation reservation = new ActivityReservation();
        reservation.setActivity(activity);
        reservation.setUser(user);
        reservation.setPeopleCount(1L);
        return activityReservationRepository.save(reservation);
    }

    public Room createRoom() {
        Amenity amenity = amenityRepository.findByName("Balcony");
        Room room = new Room();
        room.setAmenities(Set.of(amenity));
        room.setName("test-room");
        room.setArea(25.3);
        room.setPrice(BigDecimal.TEN);
        room.setNumberOfPeople(PeopleCountEnum.THREE);
        return roomRepository.save(room);
    }

    public void createRoomReservation(Room room, User user){
        RoomReservation reservation = new RoomReservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setStatus(ReservationStatusEnum.PENDING);
        reservation.setDateOfReservation(LocalDateTime.now());
        reservation.setArrivalDate(LocalDate.of(2022, 10, 10));
        reservation.setDepartureDate(LocalDate.of(2022, 10, 12));

        roomReservationRepository.save(reservation);
    }

}
