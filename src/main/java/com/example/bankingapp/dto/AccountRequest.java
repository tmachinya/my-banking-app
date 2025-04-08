package com.example.bankingapp.dto;

import com.example.bankingapp.entity.enums.AccountType;
import com.example.bankingapp.entity.enums.Currency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class AccountRequest {
    private String accountNumber;
    private AccountType accountType;
    private Currency currency;
    private BigDecimal interestRate;
    private BigDecimal overdraftLimit;
    private LocalDate maturityDate;
}