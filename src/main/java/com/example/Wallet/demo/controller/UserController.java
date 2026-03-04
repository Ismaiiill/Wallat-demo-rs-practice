package com.example.Wallet.demo.controller;


import com.example.Wallet.demo.dto.CreateUserRequestDto;
import com.example.Wallet.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody CreateUserRequestDto createUserRequestDto){
        return new ResponseEntity<>(userService.createUser(createUserRequestDto),HttpStatus.CREATED);
    }
}
