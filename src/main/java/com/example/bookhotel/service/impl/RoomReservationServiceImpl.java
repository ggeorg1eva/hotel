package com.example.bookhotel.service.impl;

import com.example.bookhotel.model.dto.RoomDto;
import com.example.bookhotel.model.dto.RoomReservationDto;
import com.example.bookhotel.model.entity.Room;
import com.example.bookhotel.model.entity.RoomReservation;
import com.example.bookhotel.model.entity.enumeration.ReservationStatusEnum;
import com.example.bookhotel.model.service.RoomReservationServiceModel;
import com.example.bookhotel.repository.RoomReservationRepository;
import com.example.bookhotel.service.RoomReservationService;
import com.example.bookhotel.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoomReservationServiceImpl implements RoomReservationService {
    private final RoomReservationRepository repository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public RoomReservationServiceImpl(RoomReservationRepository repository, ModelMapper modelMapper, UserService userService) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public void createReservation(RoomReservationServiceModel serviceModel, Room room) {
        RoomReservation res = modelMapper.map(serviceModel, RoomReservation.class);
        res.setRoom(room);
        res.setUser(userService.findUserByUsername(serviceModel.getBookerUsername()));
        repository.save(res);

    }

    @Override
    public List<RoomReservationDto> getRoomReservationsByUserid(Long id) {
        List<RoomReservationDto> dtos = repository.findAllByUser_Id(id)
                .stream()
                .map(res -> {
                    RoomReservationDto dto = modelMapper.map(res, RoomReservationDto.class);
                    dto.setRoom(modelMapper.map(res.getRoom(), RoomDto.class));
                    dto.setUserFullName(res.getUser().getFirstName() + " " + res.getUser().getLastName());
                    dto.setResStat(res.getStatus().ordinal());
                    return dto;
                })
                .toList();

        return dtos;
    }

    @Override
    public List<RoomReservationDto> getAllReservationsByStatus(ReservationStatusEnum status) {
        List<RoomReservation> reservations = repository.findAllByStatus(status);

        List<RoomReservationDto> dtos = reservations.stream()
                .map(res -> {
                    RoomReservationDto dto = modelMapper.map(res, RoomReservationDto.class);
                    dto.setRoom(modelMapper.map(res.getRoom(), RoomDto.class));
                    dto.setUserFullName(res.getUser().getFirstName() + " " + res.getUser().getLastName());
                    dto.setResStat(res.getStatus().ordinal());
                    return dto;
                })
                .toList();

        return dtos;
    }

    @Override
    public void denyReservation(LocalDateTime dateOfReservation) {
        RoomReservation res = repository.findByDateOfReservation(dateOfReservation).orElse(null);

        if (res != null) {
            res.setStatus(ReservationStatusEnum.DENIED);
            repository.save(res);
        }
    }

    @Override
    public void approveReservation(LocalDateTime dateOfReservation) {
        RoomReservation res = repository.findByDateOfReservation(dateOfReservation).orElse(null);

        if (res != null) {
            res.setStatus(ReservationStatusEnum.APPROVED);
            repository.save(res);
        }
    }

    @Override
    public void deleteAllReservationsByRoom(Room room) {
        repository.deleteAll(repository.findAllByRoom(room));
    }
}
