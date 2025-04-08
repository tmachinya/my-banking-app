package com.example.bankingapp.entity;

import com.example.bankingapp.entity.enums.AccountStatus;
import com.example.bankingapp.entity.enums.AccountType;
import com.example.bankingapp.entity.enums.Currency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType; // ✅ Added account type

    @Column(precision = 5, scale = 2)
    private BigDecimal interestRate; // ✅ For savings or fixed deposit accounts

    @Column(precision = 19, scale = 4)
    private BigDecimal overdraftLimit; // ✅ For checking accounts

    @Column
    private LocalDate maturityDate; // ✅ For fixed deposit or loan accounts

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}