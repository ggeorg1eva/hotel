package com.example.bookhotel.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.bookhotel.model.binding.PictureAddBindingModel;
import com.example.bookhotel.model.entity.Picture;
import com.example.bookhotel.repository.PictureRepository;
import com.example.bookhotel.service.PictureService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final Cloudinary cloudinary;
    private static Long picCount = 1L;

    public PictureServiceImpl(PictureRepository pictureRepository, Cloudinary cloudinary) {
        this.pictureRepository = pictureRepository;
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadPictureToCloudinary(MultipartFile picture) throws IOException {
        byte[] pictureInBytes = picture.getBytes();

        if (pictureRepository.count() != 0){
            Long lastId = pictureRepository.getLastId();
            picCount = lastId + 1;
        }

        cloudinary.uploader().upload(pictureInBytes, ObjectUtils.asMap("public_id", picCount + ""));

        return cloudinary.url().generate( picCount++ + ".jpg"); //url of uploaded picture

    }

    @Override
    public Picture addPictureToDbFromCloudinary(String url) {
        Picture picture = new Picture();
        picture.setPictureUrl(url);
        pictureRepository.save(picture);

        return pictureRepository.getByPictureUrl(url); //to return it with its id
    }

    @Override
    public void deleteSetOfPicsFromCloud(Set<Picture> pictures) throws IOException {
        List<String> urls = pictures.stream()
                .map(Picture::getPictureUrl).toList();

        for (String url : urls) {
            String nameInCloud = url.charAt(url.length() - 5) + "";

            cloudinary.uploader().destroy(nameInCloud,
                    ObjectUtils.emptyMap());
        }
    }

}
