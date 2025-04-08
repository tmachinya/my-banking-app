package com.example.bankingapp.service;

import com.example.bankingapp.entity.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account createAccount(Long userId, Account account);
    Account getAccountByNumber(String accountNumber);
    List<Account> getAccountsByUserId(Long userId);
    Account deposit(String accountNumber, BigDecimal amount);
    Account withdraw(String accountNumber, BigDecimal amount);
    void closeAccount(String accountNumber);
}
