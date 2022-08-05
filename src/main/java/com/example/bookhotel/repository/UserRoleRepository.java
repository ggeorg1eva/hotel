package com.example.bookhotel.repository;

import com.example.bookhotel.model.entity.UserRole;
import com.example.bookhotel.model.entity.enumeration.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByRole(UserRoleEnum role);
}
