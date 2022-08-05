package com.example.bookhotel.repository;

import com.example.bookhotel.model.entity.User;
import com.example.bookhotel.model.entity.enumeration.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);
    List<User> findAllByRolesRole(UserRoleEnum roles_role);
}
