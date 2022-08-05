package com.example.bookhotel.web;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleMultipartFileTooBigError(MaxUploadSizeExceededException e) {
        ModelAndView modelAndView = new ModelAndView("max-upload-size-err");
        modelAndView.addObject("error", "The field picture exceeds its maximum permitted size of 1 MB!");
        return modelAndView;
    }
}
