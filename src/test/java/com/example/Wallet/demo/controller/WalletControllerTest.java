package com.example.Wallet.demo.controller;

import com.example.Wallet.demo.service.WalletService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {
    @Mock
    private WalletService walletService;

    @InjectMocks
    private WallatController wallatController;

    @Test
    @DisplayName("Should create wallet")
    void shouldCreateWallet(){

    }
}
