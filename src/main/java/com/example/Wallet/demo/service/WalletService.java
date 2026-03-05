package com.example.Wallet.demo.service;


import com.example.Wallet.demo.dto.CreateWalletRequestDto;
import com.example.Wallet.demo.dto.DepositRequestDto;
import com.example.Wallet.demo.dto.TransferRequestDto;
import com.example.Wallet.demo.dto.WithdrawRequestDto;
import com.example.Wallet.demo.model.Transaction;
import com.example.Wallet.demo.model.TransactionType;
import com.example.Wallet.demo.model.User;
import com.example.Wallet.demo.model.Wallet;
import com.example.Wallet.demo.repository.TransactionRepository;
import com.example.Wallet.demo.repository.UserRepository;
import com.example.Wallet.demo.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public Wallet createWallet(CreateWalletRequestDto createWalletRequestDto) {
        // check if user exists or not
        User user=userRepository.findById(createWalletRequestDto.getUserid()).orElse(null);
        if(user==null){
            throw  new IllegalArgumentException("no userId exists here");
        }
        if(walletRepository.existsById(createWalletRequestDto.getUserid())){
            throw  new IllegalArgumentException("this userId already has a wallet ");
        }
        Wallet wallet=new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);

    }

    //    userid, check amount deposit
    public Wallet deposit(UUID walletId, DepositRequestDto depositRequestDto) {
        Wallet wallet=walletRepository.findById(walletId).orElse(null);
        if(wallet==null){
            throw  new IllegalArgumentException("no wallet exists here");
        }
        if (depositRequestDto.getAmount() == null || depositRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        wallet.setBalance(wallet.getBalance().add(depositRequestDto.getAmount()));
        walletRepository.save(wallet);
        //save transaction
        saveTransaction(wallet,depositRequestDto.getAmount(),TransactionType.DEPOSIT);
        return wallet;


    }


    public Wallet withdraw(UUID walletId,  WithdrawRequestDto withdrawRequestDto) {
        Wallet wallet=walletRepository.findById(walletId).orElse(null);
        if(wallet==null){
            throw  new IllegalArgumentException("no wallet exists here");
        }
        if (withdrawRequestDto.getAmount() == null || withdrawRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (wallet.getBalance().compareTo(withdrawRequestDto.getAmount())<0) {
            throw new IllegalArgumentException("you dont have that much amount");
        }
        wallet.setBalance(wallet.getBalance().subtract(withdrawRequestDto.getAmount()));
        walletRepository.save(wallet);
        //save transaction
        saveTransaction(wallet,withdrawRequestDto.getAmount(),TransactionType.WITHDRAWAL);
        return wallet;
    }


    public void transfer(TransferRequestDto transferRequestDto) {
        //toWallatId and fromWallet validation
        Wallet toWallet=walletRepository.findById(transferRequestDto.getToWalletId()).orElse(null);
        if(toWallet==null){
            throw  new IllegalArgumentException("no toWallet exists here");
        }
        Wallet fromWallet=walletRepository.findById(transferRequestDto.getFromWalletId()).orElse(null);
        if(fromWallet==null){
            throw  new IllegalArgumentException("no fromWallet exists here");
        }

        //validate money
        if (transferRequestDto.getAmount() == null || transferRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");

        }
        if (fromWallet.getBalance().compareTo(transferRequestDto.getAmount())<0) {
            throw new IllegalArgumentException("you dont have that much amount");
        }
        //check whether the to and from wallet aren't same
        if(fromWallet.getId().equals(toWallet.getId())){
            throw new IllegalArgumentException(" to and from are never be the same");
        }

        //do the job
        toWallet.setBalance(toWallet.getBalance().subtract(transferRequestDto.getAmount()));
        fromWallet.setBalance(fromWallet.getBalance().add(transferRequestDto.getAmount()));
        walletRepository.save(toWallet);
        walletRepository.save(fromWallet);
        //save transaction
        saveTransaction(toWallet,transferRequestDto.getAmount(),TransactionType.DEPOSIT);
        //save transaction
        saveTransaction(toWallet,transferRequestDto.getAmount(),TransactionType.WITHDRAWAL);

    }

    private void saveTransaction(Wallet wallet, BigDecimal amount, TransactionType transactionType){
        Transaction transaction=new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transactionRepository.save(transaction);
    }
}
