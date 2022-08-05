package com.example.bookhotel.service.impl;

import com.example.bookhotel.model.entity.Amenity;
import com.example.bookhotel.repository.AmenityRepository;
import com.example.bookhotel.service.AmenityService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AmenityServiceImpl implements AmenityService {
    private final AmenityRepository amenityRepository;
    private final ModelMapper modelMapper;

    public AmenityServiceImpl(AmenityRepository amenityRepository, ModelMapper modelMapper) {
        this.amenityRepository = amenityRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void initAmenities() {
        if (amenityRepository.count() == 0){
            List<String> list = List.of("Balcony",
                    "Air conditioning",
                    "Free wifi",
                    "Minibar",
                    "Sea view",
                    "Garden view",
                    "Pool view");

            list
                    .forEach(am -> {
                        Amenity amenity = new Amenity();
                        amenity.setName(am);
                        amenityRepository.save(amenity);
                    });
        }
    }

    @Override
    public List<String> getAllAmenities() {

        return amenityRepository.findAll()
                .stream().map(Amenity::getName)
                .collect(Collectors.toList());
    }

}
