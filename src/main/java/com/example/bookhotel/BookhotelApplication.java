package com.example.bookhotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookhotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookhotelApplication.class, args);
    }

}
