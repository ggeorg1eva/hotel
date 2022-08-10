package com.example.bookhotel.web;

import com.example.bookhotel.model.binding.ActivityAddBindingModel;
import com.example.bookhotel.model.binding.ActivityBookBindingModel;
import com.example.bookhotel.model.dto.ActivityDto;
import com.example.bookhotel.model.service.ActivityServiceModel;
import com.example.bookhotel.service.ActivityService;
import com.example.bookhotel.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/activities")
public class ActivityController {
    private final ActivityService activityService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public ActivityController(ActivityService activityService, CategoryService categoryService, ModelMapper modelMapper) {
        this.activityService = activityService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    public String addActivity(Model model) {
        model.addAttribute("categoriesView", categoryService.viewCategories());
        return "add-activity";
    }

    @ModelAttribute
    public ActivityAddBindingModel activityAddBindingModel() {
        return new ActivityAddBindingModel();
    }

    @PostMapping("/add")
    public String addActivityPost(@Valid ActivityAddBindingModel activityAddBindingModel, BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("activityAddBindingModel", activityAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.activityAddBindingModel", bindingResult);
            return "redirect:add";
        }

        ActivityServiceModel serviceModel = modelMapper.map(activityAddBindingModel, ActivityServiceModel.class);

        activityService.addActivity(serviceModel);

        return "redirect:all";
    }

    @GetMapping("/all")
    public String allActivities(Model model) {
        model.addAttribute("activities", activityService.getAllActivities());

        return "all-activities";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteActivity(@PathVariable Long id) throws IOException {
        activityService.deleteActivity(id);
        return "redirect:/activities/all";
    }

    @GetMapping("/book/{id}")
    public String bookActivity(@PathVariable Long id, Model model) {
        ActivityDto dto = activityService.getActivityById(id);

        model.addAttribute("dto", dto);
        return "book-activity";
    }

    @ModelAttribute
    public ActivityBookBindingModel activityBookBindingModel() {
        return new ActivityBookBindingModel();
    }

    @PatchMapping("/book/{id}")
    public String bookActivityPatch(@PathVariable Long id, @Valid ActivityBookBindingModel activityBookBindingModel, BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails principal) throws IOException {
        ActivityDto dto = activityService.getActivityById(id);
        Long availableSpots = dto.getAvailableSpots();
        boolean invalidPeopleCount = availableSpots - activityBookBindingModel.getPeopleCount() < 0;

        if (bindingResult.hasErrors() || invalidPeopleCount) {
            redirectAttributes.addFlashAttribute("activityBookBindingModel", activityBookBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.activityBookBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("availableSpots", availableSpots);
            redirectAttributes.addFlashAttribute("invalidPeopleCount", invalidPeopleCount);
            return "redirect:/activities/book/{id}";
        }

        activityService.bookActivity(id, principal.getUsername(), activityBookBindingModel.getPeopleCount());

        return "redirect:/users/profile";
    }
}
