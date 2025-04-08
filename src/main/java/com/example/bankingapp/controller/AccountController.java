package com.example.bankingapp.controller;

import com.example.bankingapp.dto.AccountRequest;
import com.example.bankingapp.dto.AccountResponse;
import com.example.bankingapp.entity.Account;
import com.example.bankingapp.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<AccountResponse> createAccount(@PathVariable Long userId, @RequestBody AccountRequest request) {
        Account account = new Account();
        account.setAccountNumber(request.getAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setCurrency(request.getCurrency());
        account.setInterestRate(request.getInterestRate());
        account.setOverdraftLimit(request.getOverdraftLimit());
        account.setMaturityDate(request.getMaturityDate());
        account.setBalance(BigDecimal.ZERO);

        Account createdAccount = accountService.createAccount(userId, account);

        return ResponseEntity.ok(mapToAccountResponse(createdAccount));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccountByNumber(@PathVariable String accountNumber) {
        Account account = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(mapToAccountResponse(account));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByUser(@PathVariable Long userId) {
        List<AccountResponse> responses = accountService.getAccountsByUserId(userId)
                .stream()
                .map(this::mapToAccountResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<AccountResponse> deposit(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        Account account = accountService.deposit(accountNumber, amount);
        return ResponseEntity.ok(mapToAccountResponse(account));
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        Account account = accountService.withdraw(accountNumber, amount);
        return ResponseEntity.ok(mapToAccountResponse(account));
    }

    @DeleteMapping("/{accountNumber}/close")
    public ResponseEntity<String> closeAccount(@PathVariable String accountNumber) {
        accountService.closeAccount(accountNumber);
        return ResponseEntity.ok("Account closed successfully");
    }

    private AccountResponse mapToAccountResponse(Account account) {
        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .interestRate(account.getInterestRate())
                .overdraftLimit(account.getOverdraftLimit())
                .maturityDate(account.getMaturityDate())
                .build();
    }
}
