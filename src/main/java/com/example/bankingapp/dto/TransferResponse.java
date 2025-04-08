package com.example.bankingapp.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferResponse {
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;

    public TransferResponse(String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount) {
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.amount = amount;
    }
}
