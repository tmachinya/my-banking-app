package com.example.bankingapp.service.impl;

import com.example.bankingapp.service.AccountService;
import com.example.bankingapp.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {

    private final AccountService accountService;

    @Override
    @Transactional
    public void transferAmount(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        accountService.withdraw(fromAccountNumber, amount);
        accountService.deposit(toAccountNumber, amount);
    }
}