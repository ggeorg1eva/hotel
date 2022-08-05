package com.example.bookhotel.service.impl;

import com.example.bookhotel.model.dto.ActivityDto;
import com.example.bookhotel.model.entity.Activity;
import com.example.bookhotel.model.entity.ActivityCategory;
import com.example.bookhotel.model.entity.Picture;
import com.example.bookhotel.model.service.ActivityServiceModel;
import com.example.bookhotel.repository.ActivityRepository;
import com.example.bookhotel.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;
    private final PictureService pictureService;
    private final ActivityReservationService activityReservationService;

    public ActivityServiceImpl(ActivityRepository activityRepository, ModelMapper modelMapper, CategoryService categoryService, PictureService pictureService, ActivityReservationService activityReservationService) {
        this.activityRepository = activityRepository;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.pictureService = pictureService;
        this.activityReservationService = activityReservationService;
    }

    @Override
    public void addActivity(ActivityServiceModel model) throws IOException {
        Activity activity = modelMapper.map(model, Activity.class);

        Set<ActivityCategory> categories = model.getCategories()
                .stream()
                .map(categoryService::findCategoryByName)
                .collect(Collectors.toSet());

        activity.setCategories(categories);

        String url = pictureService.uploadPictureToCloudinary(model.getPicture());

        Picture picture = pictureService.addPictureToDbFromCloudinary(url);


        activity.setPicture(picture);

        activityRepository.save(activity);

    }

    @Override
    public List<ActivityDto> getAllActivities() {
        return activityRepository.findAll()
                .stream()
                .map(activity -> {
                    ActivityDto dto = modelMapper.map(activity, ActivityDto.class);
                    dto.setPictureUrl(activity.getPicture().getPictureUrl());
                    dto.setCategories(activity.getCategories()
                            .stream()
                            .map(ActivityCategory::getName)
                            .collect(Collectors.toSet()));

                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public void deleteActivity(Long id) throws IOException {
        Activity activity = activityRepository.findById(id).orElse(null);

        if (activity != null){
            pictureService.deleteSetOfPicsFromCloud(Set.of(activity.getPicture()));
            activityRepository.delete(activity);
        }
    }

    @Override
    public ActivityDto getActivityById(Long id) {
        Activity activity = activityRepository.findById(id).orElse(null);

        ActivityDto dto = modelMapper.map(activity, ActivityDto.class);

        dto.setPictureUrl(activity.getPicture().getPictureUrl());

        dto.setCategories(activity.getCategories()
                .stream()
                .map(ActivityCategory::getName)
                .collect(Collectors.toSet()));

        return dto;

    }


    @Override
    public void bookActivity(Long id, String username, Long peopleCount) {
        Activity activity = activityRepository.findById(id).orElse(null);

        if (activity != null){
            activity.setAvailableSpots(activity.getAvailableSpots() - peopleCount); //reduce the free spots
            activityReservationService.createActivityReservation(activity, username, peopleCount); // make reservation for this activity
        }
    }

    @Override
    public int deleteOlderActivities(LocalDate now) {
        List<Activity> activities = activityRepository.findAllByDateBefore(now);
        int count = activities.size();

        activities.stream()
                .forEach(activity -> {
                    activityReservationService.deleteAllReservationsByActivity(activity);
                    activityRepository.delete(activity);
                });

        return count;
    }
}
