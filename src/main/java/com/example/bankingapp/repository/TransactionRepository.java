package com.example.bankingapp.repository;

import com.example.bankingapp.entity.Transaction;
import com.example.bankingapp.entity.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findByAccountIdAndTransactionType(Long accountId, TransactionType transactionType);

    List<Transaction> findByAccountIdAndTransactionDateBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}