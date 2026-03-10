package com.example.Wallet.demo.controller;

import com.example.Wallet.demo.dto.CreateWalletRequestDto;
import com.example.Wallet.demo.model.User;
import com.example.Wallet.demo.model.Wallet;
import com.example.Wallet.demo.service.WalletService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {
    @Mock
    private WalletService walletService;

    @InjectMocks
    private WallatController wallatController;

    @Test
    @DisplayName("Should create wallet")
    void shouldCreateWallet(){
        User user=new User(UUID.randomUUID(),"testval","testval123@gmail.com",null);
        CreateWalletRequestDto request=new CreateWalletRequestDto(user.getId());
        Wallet wallet=new Wallet(UUID.randomUUID(), BigDecimal.ZERO, user,null,1L);

        when(walletService.createWallet(request)).thenReturn(wallet);

        ResponseEntity<Wallet> response=wallatController.createWallet(request);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(wallet,response.getBody());
//        verify(walletService,times(1)).createWallet(user.getId());

    }
}
