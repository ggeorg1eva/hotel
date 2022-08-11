package com.example.bookhotel.service;

import com.example.bookhotel.model.entity.User;
import com.example.bookhotel.model.entity.UserRole;
import com.example.bookhotel.model.entity.enumeration.UserRoleEnum;
import com.example.bookhotel.repository.UserRepository;
import com.example.bookhotel.repository.UserRoleRepository;
import com.example.bookhotel.service.impl.OperatorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OperatorServiceTest {
    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserRoleRepository mockUserRoleRepository;

    @Mock
    private ModelMapper modelMapper;

    private OperatorService operatorService;

    @BeforeEach
    public void setUp(){
        this.operatorService = new OperatorServiceImpl(mockUserRepository, mockUserRoleRepository, modelMapper);
    }

    @Test
    public void testRemoveOperatorRole_WithExistingId_Successful(){
        UserRole operatorRole = new UserRole();
        operatorRole.setRole(UserRoleEnum.OPERATOR);

        UserRole userRole = new UserRole();
        operatorRole.setRole(UserRoleEnum.USER);

        Set roles = new HashSet();
        roles.add(operatorRole);
        roles.add(userRole);

        User userInDb = new User();
        userInDb.setUsername("test-operator");
        userInDb.setFirstName("Operator");
        userInDb.setLastName("Operatorov");
        userInDb.setAge(35);
        userInDb.setPassword("1234");
        userInDb.setEmail("toperator@gmail.com");
        userInDb.setRoles(roles); // because using Set.of() returns an immutable collection and roles cannot be removed

        when(mockUserRepository.findById(1L))
                .thenReturn(Optional.of(userInDb));

        when(mockUserRoleRepository.findByRole(UserRoleEnum.OPERATOR))
                .thenReturn(operatorRole);


        operatorService.removeOperatorRole(1L);

        assertEquals(1, userInDb.getRoles().size());
        assertFalse(userInDb.getRoles().contains(operatorRole));

    }

}
