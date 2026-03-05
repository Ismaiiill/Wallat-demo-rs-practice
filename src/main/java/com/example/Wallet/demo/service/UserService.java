package com.example.Wallet.demo.service;


import com.example.Wallet.demo.dto.CreateUserRequestDto;
import com.example.Wallet.demo.model.User;
import com.example.Wallet.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final Logger log= LoggerFactory.getLogger("sfs");

    public String createUser(CreateUserRequestDto createUserRequestDto) {
        User user=new User();
        user.setEmail(createUserRequestDto.getEmail().toLowerCase());
        user.setUsername(createUserRequestDto.getUsername().trim());


        if(userRepository.existsByUsernameIgnoreCase(user.getUsername())){
            log.error("User name already exists :{}");
            throw new IllegalArgumentException("Username already exists");
        }

        if(userRepository.existsByEmailIgnoreCase(user.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }

        userRepository.save(user);

        return "creted this user "+ user.getId();
    }
}
