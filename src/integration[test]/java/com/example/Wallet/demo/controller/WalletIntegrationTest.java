package com.example.Wallet.demo.controller;


import com.example.Wallet.demo.BaseIntegrationTest;
import com.example.Wallet.demo.dto.CreateWalletRequestDto;
import com.example.Wallet.demo.model.User;
import com.example.Wallet.demo.model.Wallet;
import com.example.Wallet.demo.repository.TransactionRepository;
import com.example.Wallet.demo.repository.UserRepository;
import com.example.Wallet.demo.repository.WalletRepository;
import com.example.Wallet.demo.service.UserService;
import com.example.Wallet.demo.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WalletIntegrationTest extends BaseIntegrationTest {
    private final RestTemplate restTemplate=new RestTemplate();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

//    private UUID senderWalletId;
//    private UUID reciverWalletId;

    @Test
    void shouldCreateWalletForExistingUser(){
        User user=new User();
        user.setUsername("walletuser");
        user.setEmail("wallet@example.com");
        userRepository.save(user);

        CreateWalletRequestDto request=new CreateWalletRequestDto();
        request.setUserid(user.getId());

        String url="http://localhost:"+port+"/wallets";
        ResponseEntity<Wallet> response=restTemplate.postForEntity(url,request,Wallet.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void shouldReturnNotFoundForNonExistingUser(){
        CreateWalletRequestDto request=new CreateWalletRequestDto();
        request.setUserid(UUID.randomUUID());

        String url="http://localhost:"+port+"/wallets";

        try {
            restTemplate.postForEntity(url,request,Wallet.class);
        }catch (HttpClientErrorException e){
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    void shouldDepositMoneySucessfully() throws Exception{

        User user=userRepository.save(new User(null,"john","john@gmail.com",null));

        Wallet wallet=walletRepository.save(new Wallet(null,user, BigDecimal.ZERO));

        UUID walledId=wallet.getId();

        mockMvc.perform(post("/wallets/{id}/deposit",walledId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                { "amount": 50 }
                        """))
                .andExpect(status().isOk());
        Wallet updated=walletRepository.findById(walledId).orElseThrow();
        assertEquals(new BigDecimal("50.00"), updated.getBalance());
        assertEquals(1,transactionRepository.findAll().size());
    }

    @Test
    void shouldFailWithdrawWhenInsufficientFunds() throws Exception{
        User user=userRepository.save(new User(null,"john","john@gmail.com",null));

        Wallet wallet=walletRepository.save(new Wallet(null,user, new BigDecimal(30)));

        UUID walletId=wallet.getId();
        mockMvc.perform(post("wallets/{id}/withdraw",walletId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                { "amount": 50 }
                        """))
                .andExpect(status().isBadRequest());

        Wallet reloaded=walletRepository.findById(walletId).orElseThrow();

        assertEquals(new BigDecimal(30),wallet.getBalance());
        assertEquals(0,transactionRepository.count());
    }


}
