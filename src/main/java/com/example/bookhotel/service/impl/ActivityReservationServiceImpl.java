package com.example.bookhotel.service.impl;

import com.example.bookhotel.model.dto.ActivityReservationDto;
import com.example.bookhotel.model.entity.Activity;
import com.example.bookhotel.model.entity.ActivityReservation;
import com.example.bookhotel.repository.ActivityReservationRepository;
import com.example.bookhotel.service.ActivityReservationService;
import com.example.bookhotel.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityReservationServiceImpl implements ActivityReservationService {
    private final ActivityReservationRepository activityReservationRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public ActivityReservationServiceImpl(ActivityReservationRepository activityReservationRepository, UserService userService, ModelMapper modelMapper) {
        this.activityReservationRepository = activityReservationRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createActivityReservation(Activity activity, String username, Long peopleCount) {
        ActivityReservation reservation = activityReservationRepository.findByActivityAndUser_Username(activity, username).orElse(null);

        if (reservation != null) {
            reservation.setPeopleCount(reservation.getPeopleCount() + peopleCount); // update reservation in db if this user already booked same activity
        } else {
            reservation = new ActivityReservation();
            reservation.setActivity(activity);
            reservation.setUser(userService.findUserByUsername(username));
            reservation.setPeopleCount(peopleCount);

        }

        activityReservationRepository.save(reservation);
    }

    @Override
    public List<ActivityReservationDto> getActivityReservationsByUserId(Long id) {
        List<ActivityReservationDto> dtos = activityReservationRepository.findAllByUser_Id(id)
                .stream()
                .map(reservation -> {
                    ActivityReservationDto dto = modelMapper.map(reservation, ActivityReservationDto.class);

                    dto.setActivityName(reservation.getActivity().getName());

                    dto.setPeopleCount(reservation.getPeopleCount());

                    dto.setActivityDate(reservation.getActivity().getDate());
                    return dto;
                })
                .toList();

        return dtos;
    }

    @Override
    public void deleteAllReservationsByActivity(Activity activity) {
        activityReservationRepository.deleteAll(activityReservationRepository.findAllByActivity(activity));
    }
}
