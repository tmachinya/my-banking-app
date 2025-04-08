package com.example.bankingapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
}