package com.example.bookhotel.service.impl;

import com.example.bookhotel.model.binding.PictureAddBindingModel;
import com.example.bookhotel.model.binding.RoomBookBindingModel;
import com.example.bookhotel.model.dto.RoomDto;
import com.example.bookhotel.model.entity.Amenity;
import com.example.bookhotel.model.entity.Picture;
import com.example.bookhotel.model.entity.Room;
import com.example.bookhotel.model.service.RoomReservationServiceModel;
import com.example.bookhotel.model.service.RoomServiceModel;
import com.example.bookhotel.repository.AmenityRepository;
import com.example.bookhotel.repository.RoomRepository;
import com.example.bookhotel.service.PictureService;
import com.example.bookhotel.service.RoomReservationService;
import com.example.bookhotel.service.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final AmenityRepository amenityRepository;
    private final ModelMapper modelMapper;
    private final PictureService pictureService;
    private final RoomReservationService roomReservationService;

    public RoomServiceImpl(RoomRepository roomRepository, AmenityRepository amenityRepository, ModelMapper modelMapper, PictureService pictureService, RoomReservationService roomReservationService) {
        this.roomRepository = roomRepository;
        this.amenityRepository = amenityRepository;
        this.modelMapper = modelMapper;
        this.pictureService = pictureService;
        this.roomReservationService = roomReservationService;
    }

    @Override
    public void addRoom(RoomServiceModel model) {

        Room room = modelMapper.map(model, Room.class);

        Set<Amenity> amenities = model.getAmenities()
                .stream()
                .map(amenityRepository::findByName)
                .collect(Collectors.toSet());

        room.setAmenities(amenities);

        roomRepository.save(room);

    }

    @Override
    public List<RoomDto> getAllRooms() {
        List<RoomDto> roomDtos = roomRepository.findAll()
                .stream()
                .map(room -> {
                    RoomDto dto = modelMapper.map(room, RoomDto.class);
                    dto.setAmenities(room.getAmenities()
                            .stream()
                            .map(Amenity::getName)
                            .collect(Collectors.toSet()));

                    if (room.getPictures() != null && !room.getPictures().isEmpty()) {
                        dto.setFirstPicUrl(room.getPictures()
                                .stream()
                                .findFirst()
                                .map(Picture::getPictureUrl)
                                .orElse(""));
                    }
                    return dto;
                })
                .toList();
        return roomDtos;
    }

    @Override
    public void addPictureToRoom(PictureAddBindingModel pictureAddBindingModel, Long id) throws IOException {
        String url = pictureService.uploadPictureToCloudinary(pictureAddBindingModel.getPicture());

        Picture picture = pictureService.addPictureToDbFromCloudinary(url);

        Room room = roomRepository.findById(id).orElse(null);

        if (room != null) {
            room.getPictures().add(picture);
            roomRepository.save(room); // patch
        }
    }

    @Override
    public RoomDto getRoomById(Long id) {
        Room entity = roomRepository.findById(id).orElse(null);

        if (entity == null) {
            return null;
        }

        RoomDto dto = modelMapper.map(entity, RoomDto.class);

        dto.setAmenities(entity.getAmenities()
                .stream()
                .map(Amenity::getName)
                .collect(Collectors.toSet()));

        if (entity.getPictures() != null && !entity.getPictures().isEmpty()) {
            dto.setPictureUrls(entity.getPictures()
                    .stream()
                    .map(Picture::getPictureUrl)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    @Override
    public void deleteRoom(Long id) throws IOException {
        Room room = roomRepository.findById(id).orElse(null);

        if (room != null) {
            pictureService.deleteSetOfPicsFromCloud(room.getPictures());
            roomReservationService.deleteAllReservationsByRoom(room);
            roomRepository.delete(room);
        }
    }

    @Override
    public void bookRoom(RoomReservationServiceModel serviceModel) {
        Room room = roomRepository.findById(serviceModel.getRoomId()).orElse(null);
        if (room != null){
            roomReservationService.createReservation(serviceModel, room); // create the reservation
        }
    }


}
