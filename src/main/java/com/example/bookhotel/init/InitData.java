package com.example.bookhotel.init;

import com.example.bookhotel.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitData implements CommandLineRunner {
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final CategoryService categoryService;
    private final AmenityService amenityService;

    public InitData(UserService userService, UserRoleService userRoleService, CategoryService categoryService, AmenityService amenityService ) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.categoryService = categoryService;
        this.amenityService = amenityService;
    }

    @Override
    public void run(String... args) throws Exception {
        userRoleService.initRoles();
        userService.initUsers();
        categoryService.initCategories();
        amenityService.initAmenities();
    }
}
