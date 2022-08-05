package com.example.bookhotel.service;

import com.example.bookhotel.model.dto.OperatorDto;

import java.util.List;

public interface OperatorService {
    List<OperatorDto> getAllOperators();

    void removeOperatorRole(Long id);
}
