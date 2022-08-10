package com.example.bookhotel.web;

import com.example.bookhotel.model.binding.CategoryAddBindingModel;
import com.example.bookhotel.service.ActivityService;
import com.example.bookhotel.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ActivityService activityService;
    private final ModelMapper modelMapper;

    public CategoryController(CategoryService categoryService, ActivityService activityService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.activityService = activityService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public String allCats(Model model){
        model.addAttribute("cats", categoryService.viewCategories());
        return "view-categories";
    }

    @GetMapping("/add")
    public String addCategory(){
        return "add-category";
    }

    @ModelAttribute
    public CategoryAddBindingModel categoryAddBindingModel(){
        return new CategoryAddBindingModel();
    }

    @PostMapping("/add")
    public String addCategoryPost(@Valid CategoryAddBindingModel categoryAddBindingModel, BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("categoryAddBindingModel", categoryAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoryAddBindingModel",
                    bindingResult);

            return "redirect:add";
        }

        if (!categoryService.addCategory(categoryAddBindingModel.getName())){
            return "redirect:add";
        }

        return "redirect:all";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id){
        //if activity has only one category, it is deleted with it; if it has more categories - the one cat, being deleted, is removed
        activityService.removeCategoryFromActivity(categoryService.findCategoryById(id));
        categoryService.deleteCategory(id);

        return "redirect:/categories/all";
    }

}
