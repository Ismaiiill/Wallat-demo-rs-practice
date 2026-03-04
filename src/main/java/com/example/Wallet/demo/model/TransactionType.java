package com.example.Wallet.demo.model;


import lombok.Getter;
import lombok.Setter;

@Getter
public enum TransactionType {
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER_IN,
    TRANSFER_OUT
}
