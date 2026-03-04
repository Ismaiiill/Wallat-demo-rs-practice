package com.example.Wallet.demo.service;


import com.example.Wallet.demo.dto.CreateUserRequestDto;
import com.example.Wallet.demo.model.User;
import com.example.Wallet.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public String createUser(CreateUserRequestDto createUserRequestDto) {
        User user=new User();
        user.setEmail(createUserRequestDto.getEmail().toLowerCase());
        user.setUsername(createUserRequestDto.getUsername().trim());


        if(userRepository.existsByUsernameIgnoreCase(user.getUsername())){
            throw new IllegalArgumentException("Username already exists");
        }

        if(userRepository.existsByEmailIgnoreCase(user.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }

        userRepository.save(user);

        return "creted this user "+ user.getId();
    }
}
