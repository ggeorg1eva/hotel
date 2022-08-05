package com.example.bookhotel.service;


import com.example.bookhotel.model.entity.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

public interface PictureService {
    String uploadPictureToCloudinary(MultipartFile picture) throws IOException;
    Picture addPictureToDbFromCloudinary(String url);

    void deleteSetOfPicsFromCloud(Set<Picture> pictures) throws IOException;

}
