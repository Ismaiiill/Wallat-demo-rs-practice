package com.example.Wallet.demo.controller;

import com.example.Wallet.demo.dto.CreateUserRequestDto;
import com.example.Wallet.demo.model.User;
import com.example.Wallet.demo.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("should create user")
    void shouldCreateUser(){
        CreateUserRequestDto request=new CreateUserRequestDto("testuser","ismail123@gmail.com");
        User createdUser= new User(UUID.randomUUID(),"testuser","ismail123@gmail.com",null);

        when(userService.createUser(any(CreateUserRequestDto.class))).thenReturn(createdUser);

        ResponseEntity<User> response=userController.createUser(request);
        assertEquals(201,response.getStatusCode().value());
        assertEquals(createdUser,response.getBody());

        verify(userService,times(1)).createUser(any(CreateUserRequestDto.class));
    }
}
