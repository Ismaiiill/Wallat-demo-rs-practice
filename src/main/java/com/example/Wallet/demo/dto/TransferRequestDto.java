package com.example.Wallet.demo.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TransferRequestDto {
    @NotNull
    private UUID fromWalletId;

    @NotNull
    private UUID toWalletId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;
}
