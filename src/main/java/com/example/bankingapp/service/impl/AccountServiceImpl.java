package com.example.bankingapp.service.impl;

import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.entity.enums.AccountStatus;
import com.example.bankingapp.exception.InsufficientFundsException;
import com.example.bankingapp.exception.ResourceNotFoundException;
import com.example.bankingapp.repository.AccountRepository;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public Account createAccount(Long userId, Account account) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        account.setUser(user); // ✅ Set the user

        account.setBalance(BigDecimal.ZERO); // initialize balance
        account.setStatus(AccountStatus.ACTIVE);

        return accountRepository.save(account);
    }


    @Override
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));
    }

    @Override
    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Account deposit(String accountNumber, BigDecimal amount) {
        Account account = getAccountByNumber(accountNumber);
        account.setBalance(account.getBalance().add(amount));
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }

        account.setBalance(account.getBalance().subtract(amount));
        return accountRepository.save(account);
    }


    @Override
    public void closeAccount(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);
    }
}