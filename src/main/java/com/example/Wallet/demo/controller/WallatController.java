package com.example.Wallet.demo.controller;


import com.example.Wallet.demo.dto.CreateWalletRequestDto;
import com.example.Wallet.demo.dto.DepositRequestDto;
import com.example.Wallet.demo.dto.TransferRequestDto;
import com.example.Wallet.demo.dto.WithdrawRequestDto;
import com.example.Wallet.demo.model.Wallet;
import com.example.Wallet.demo.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WallatController {
    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@Valid @RequestBody CreateWalletRequestDto createWalletRequestDto){
        return new ResponseEntity<>(walletService.createWallet(createWalletRequestDto), HttpStatus.CREATED);
    }
    //get balance
    //get all transaction

    //deposit
    @PostMapping("/deposit/{walletId}")
    public ResponseEntity<Wallet> deposit(@PathVariable UUID walletId,
                                          @Valid @RequestBody DepositRequestDto depositRequestDto){
        return new ResponseEntity<>(walletService.deposit(walletId, depositRequestDto), HttpStatus.CREATED);
    }

    //withdraw
    @PostMapping("/withdraw/{walletId}")
    public ResponseEntity<Wallet> withdraw(@PathVariable UUID walletId,
                                          @Valid @RequestBody WithdrawRequestDto withdrawRequestDto){
        return new ResponseEntity<>(walletService.withdraw(walletId, withdrawRequestDto), HttpStatus.CREATED);
    }

    //transfer
    @PostMapping("/transfer")
    public ResponseEntity<Wallet> transfer(@Valid @RequestBody TransferRequestDto transferRequestDto){
        return new ResponseEntity<>(walletService.transfer(transferRequestDto), HttpStatus.OK);
    }
}
