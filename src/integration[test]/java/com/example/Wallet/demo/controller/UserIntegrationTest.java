package com.example.Wallet.demo.controller;

import com.example.Wallet.demo.BaseIntegrationTest;
import com.example.Wallet.demo.dto.CreateUserRequestDto;
import com.example.Wallet.demo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserIntegrationTest extends BaseIntegrationTest {

    private final RestTemplate restTemplate=new RestTemplate();

    @Test
    void shouldCreateUser(){
        CreateUserRequestDto requestDto=new CreateUserRequestDto("testuser","user123@gmail.com");
        String url="http://localhost:"+port+"/users";

        ResponseEntity<User> response=restTemplate.postForEntity(url,requestDto,User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("testuser");
        assertThat(response.getBody().getEmail()).isEqualTo("user123@gmail.com");
    }

    @Test
    void shouldCreateUserAndIgnoreProvidedId(){
        UUID providedId=UUID.randomUUID();
        String jsonRequest = String.format("{\"id\":\"%s\", \"username\":\"testuser2\", \"email\":\"test2@example.com\"}", providedId);

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity=new HttpEntity<>(jsonRequest,headers);

        String url="http://localhost:"+port+"/users";
        ResponseEntity<User>response=restTemplate.postForEntity(url,entity,User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat((response.getBody().getId())).isEqualTo(providedId);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("testuser2");
    }
}
