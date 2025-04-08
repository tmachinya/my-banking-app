package com.example.bankingapp.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

public interface TransferService {
    void transferAmount(String fromAccountNumber, String toAccountNumber, BigDecimal amount);
}