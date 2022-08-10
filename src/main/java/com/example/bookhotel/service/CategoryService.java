package com.example.bookhotel.service;

import com.example.bookhotel.model.dto.CategoryDto;
import com.example.bookhotel.model.entity.ActivityCategory;
import jdk.jfr.Category;

import java.util.List;

public interface CategoryService {
    void initCategories();

    boolean addCategory(String name);

    List<CategoryDto> viewCategories();

    void deleteCategory(Long id);

    ActivityCategory findCategoryByName(String cat);

    ActivityCategory findCategoryById(Long id);
}
