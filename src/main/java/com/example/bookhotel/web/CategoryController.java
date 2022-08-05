package com.example.bookhotel.web;

import com.example.bookhotel.model.binding.CategoryAddBindingModel;
import com.example.bookhotel.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
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

        categoryService.deleteCategory(id);

        //todo add logic when there are activities in this cat

        return "redirect:/categories/all";
    }

}
