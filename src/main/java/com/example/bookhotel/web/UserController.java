package com.example.bookhotel.web;

import com.example.bookhotel.model.binding.UserRegisterBindingModel;
import com.example.bookhotel.model.dto.ActivityReservationDto;
import com.example.bookhotel.model.dto.RoomReservationDto;
import com.example.bookhotel.model.dto.UserDto;
import com.example.bookhotel.model.service.UserServiceModel;
import com.example.bookhotel.service.ActivityReservationService;
import com.example.bookhotel.service.RoomReservationService;
import com.example.bookhotel.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ActivityReservationService activityReservationService;
    private final RoomReservationService roomReservationService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, ActivityReservationService activityReservationService, RoomReservationService roomReservationService, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.activityReservationService = activityReservationService;
        this.roomReservationService = roomReservationService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String userName,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, userName);
        redirectAttributes.addFlashAttribute("incorrectInput",
                true);

        return "redirect:/login";
    }

    @GetMapping("/register")
    private String register(){
        return "register";
    }

    @ModelAttribute
    public UserRegisterBindingModel userRegisterBindingModel(){
        return new UserRegisterBindingModel();
    }

    @PostMapping("/register")
    private String registerPost(@Valid UserRegisterBindingModel userRegisterBindingModel,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors() || !userRegisterBindingModel.getPassword().equals(
                userRegisterBindingModel.getConfirmPassword()
        )){
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel", bindingResult);
            return "redirect:register";
        }

        if (userService.isUserExist(userRegisterBindingModel.getUsername(),
                userRegisterBindingModel.getEmail())){
            redirectAttributes.addFlashAttribute("userExist", true);
            return "redirect:register";
        }

        UserServiceModel serviceModel = modelMapper.map(userRegisterBindingModel, UserServiceModel.class);
        serviceModel.setPassword(passwordEncoder.encode(userRegisterBindingModel.getPassword()));
        userService.registerUser(serviceModel);

        return "redirect:login";
    }

    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal UserDetails principal, Model model){

        UserDto userDto = userService.getDtoByUsername(principal.getUsername()); // get currently logged-in user
        List<ActivityReservationDto> activityReservationDtos = activityReservationService.getActivityReservationsByUserId(userDto.getId());
        List<RoomReservationDto> roomReservationDtos = roomReservationService.getRoomReservationsByUserid(userDto.getId());


        model.addAttribute("user", userDto);
        model.addAttribute("activityReservations", activityReservationDtos);
        model.addAttribute("roomReservations", roomReservationDtos);


        return "user-profile";
    }
}
