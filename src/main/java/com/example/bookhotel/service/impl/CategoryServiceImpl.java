package com.example.bookhotel.service.impl;

import com.example.bookhotel.model.dto.CategoryDto;
import com.example.bookhotel.model.entity.ActivityCategory;
import com.example.bookhotel.repository.CategoryRepository;
import com.example.bookhotel.service.ActivityService;
import com.example.bookhotel.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }


    @Override
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

    @Override
    public boolean addCategory(String name) {
        Optional<ActivityCategory> catInDb = categoryRepository.findByName(name);

        if (catInDb.isPresent()) {
            return false;
        }

        ActivityCategory category = new ActivityCategory();

        category.setName(name);

        categoryRepository.save(category);
        return true;


    }

    @Override
    public List<CategoryDto> viewCategories() {
        return categoryRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(ActivityCategory::getId))
                .map(cat -> modelMapper.map(cat, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long id) {
        ActivityCategory cat = categoryRepository.findById(id).orElse(null);

        if (cat != null) {
            categoryRepository.delete(cat);
        }
    }

    @Override
    public ActivityCategory findCategoryByName(String cat) {
        return categoryRepository.findByName(cat).orElse(null);
    }
}
