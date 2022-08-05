package com.example.bookhotel.service;

import com.example.bookhotel.model.binding.PictureAddBindingModel;
import com.example.bookhotel.model.binding.RoomBookBindingModel;
import com.example.bookhotel.model.dto.RoomDto;
import com.example.bookhotel.model.service.RoomReservationServiceModel;
import com.example.bookhotel.model.service.RoomServiceModel;

import java.io.IOException;
import java.util.List;

public interface RoomService {
    void addRoom(RoomServiceModel model);
    List<RoomDto> getAllRooms();

    void addPictureToRoom(PictureAddBindingModel pictureAddBindingModel, Long id) throws IOException;

    RoomDto getRoomById(Long id);

    void deleteRoom(Long id) throws IOException;


    void bookRoom(RoomReservationServiceModel map);
}
