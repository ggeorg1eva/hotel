package com.example.bookhotel.web;

import com.example.bookhotel.model.binding.RoomAddBindingModel;
import com.example.bookhotel.model.binding.PictureAddBindingModel;
import com.example.bookhotel.model.binding.RoomBookBindingModel;
import com.example.bookhotel.model.dto.RoomDto;
import com.example.bookhotel.model.entity.enumeration.ReservationStatusEnum;
import com.example.bookhotel.model.service.RoomReservationServiceModel;
import com.example.bookhotel.model.service.RoomServiceModel;
import com.example.bookhotel.service.AmenityService;
import com.example.bookhotel.service.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;
    private final AmenityService amenityService;
    private final ModelMapper modelMapper;

    public RoomController(RoomService roomService, AmenityService amenityService, ModelMapper modelMapper) {
        this.roomService = roomService;
        this.amenityService = amenityService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    public String addRoom(Model model) {
        model.addAttribute("amenitiesView", amenityService.getAllAmenities());
        return "add-room";
    }

    @ModelAttribute
    public RoomAddBindingModel roomAddBindingModel() {
        return new RoomAddBindingModel();
    }

    @PostMapping("/add")
    public String addRoomPost(@Valid RoomAddBindingModel roomAddBindingModel, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("roomAddBindingModel", roomAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.roomAddBindingModel", bindingResult);
            return "redirect:add";
        }

        roomService.addRoom(modelMapper.map(roomAddBindingModel, RoomServiceModel.class));

        return "redirect:all";
    }

    @GetMapping("/all")
    public String allRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());

        return "all-rooms";
    }

    @GetMapping("/{id}")
    public String viewRoom(@PathVariable Long id, Model model) {
        RoomDto roomDto = roomService.getRoomById(id);

        model.addAttribute("room", roomDto);

        return "view-room";
    }

    @ModelAttribute
    public PictureAddBindingModel roomPictureAddBindingModel() {
        return new PictureAddBindingModel();
    }

    @PatchMapping("/{id}/addPicture")
    public String addPicturePatch(@PathVariable Long id, @Valid PictureAddBindingModel pictureAddBindingModel,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {
        if (pictureAddBindingModel.getPicture().getSize() > 1048576){
            throw new MaxUploadSizeExceededException(1048576); // allowed multipart file size is 1 MB
        }

        if (bindingResult.hasErrors() || pictureAddBindingModel.getPicture().isEmpty()) {
            redirectAttributes.addFlashAttribute("roomPictureAddBindingModel", pictureAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.roomPictureAddBindingModel", bindingResult);
            return "redirect:";
        }

        roomService.addPictureToRoom(pictureAddBindingModel, id);
        return "redirect:";
    }


    @DeleteMapping("/{id}/delete")
    private String deleteRoom(@PathVariable Long id) throws IOException {

        roomService.deleteRoom(id);

        return "redirect:/rooms/all";
    }

    @GetMapping("/{id}/reserve")
    public String bookRoom(@PathVariable Long id, Model model){

        RoomDto roomDto = roomService.getRoomById(id);

        model.addAttribute("room", roomDto);

        return "book-room";
    }

    @ModelAttribute
    public RoomBookBindingModel roomBookBindingModel(){
        return new RoomBookBindingModel();
    }

    @PostMapping("/{id}/reserve")
    public String bookRoomPost(@Valid RoomBookBindingModel roomBookBindingModel, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes, @PathVariable Long id,
                               @AuthenticationPrincipal UserDetails principal){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("roomBookBindingModel", roomBookBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.roomBookBindingModel", bindingResult);
            return "redirect:/rooms/{id}/reserve";
        }

        if (roomBookBindingModel.getDepartureDate().isBefore(roomBookBindingModel.getArrivalDate())){
            redirectAttributes.addFlashAttribute("arrivalAfterDepartDate", true);
            return "redirect:/rooms/{id}/reserve";
        }

        roomBookBindingModel.setDateOfReservation(LocalDateTime.now());
        roomBookBindingModel.setStatus(ReservationStatusEnum.PENDING);
        roomBookBindingModel.setRoomId(id);
        roomBookBindingModel.setBookerUsername(principal.getUsername());

        roomService.bookRoom(modelMapper.map(roomBookBindingModel, RoomReservationServiceModel.class));

        return "redirect:/users/profile";
    }
}
