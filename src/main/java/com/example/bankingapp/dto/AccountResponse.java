package com.example.bankingapp.dto;

import com.example.bankingapp.entity.enums.AccountStatus;
import com.example.bankingapp.entity.enums.AccountType;
import com.example.bankingapp.entity.enums.Currency;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class AccountResponse {
    private String accountNumber;
    private BigDecimal balance;
    private Currency currency;
    private AccountType accountType;
    private AccountStatus status;
    private BigDecimal interestRate;
    private BigDecimal overdraftLimit;
    private LocalDate maturityDate;
}
