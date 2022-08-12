package com.example.bookhotel.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.SingletonManager;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

@Configuration
public class ApplicationBeanConfiguration {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public Cloudinary cloudinary(){
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "ggeorgieva",
                "api_key", "237187435455454",
                "api_secret", "8mhXiV0ZhBKhZKOXqOfLwet62Dk"));

        SingletonManager manager = new SingletonManager();
        manager.setCloudinary(cloudinary);
        manager.init();

        return Singleton.getCloudinary();
    }


}
