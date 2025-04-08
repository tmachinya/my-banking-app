package com.example.bankingapp.repository;

import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.enums.AccountStatus;
import com.example.bankingapp.entity.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByUserIdAndStatus(Long userId, AccountStatus status);

    List<Account> findByAccountType(AccountType accountType);

    List<Account> findByBalanceGreaterThanEqual(BigDecimal balance);

    List<Account> findByUserId(Long userId);

    Boolean existsByAccountNumber(String accountNumber);
}
