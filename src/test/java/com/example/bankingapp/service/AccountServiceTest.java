package com.example.bankingapp.service;

import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.entity.enums.AccountType;
import com.example.bankingapp.entity.enums.Currency;
import com.example.bankingapp.exception.InsufficientFundsException;
import com.example.bankingapp.exception.ResourceNotFoundException;
import com.example.bankingapp.repository.AccountRepository;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeposit() {
        // Given
        Account account = new Account();
        account.setAccountNumber("ACC10001");
        account.setBalance(BigDecimal.valueOf(100));

        when(accountRepository.findByAccountNumber("ACC10001")).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        // When
        Account updatedAccount = accountService.deposit("ACC10001", BigDecimal.valueOf(200));

        // Then
        assertEquals(BigDecimal.valueOf(300), updatedAccount.getBalance());
        verify(accountRepository, times(1)).save(account);
    }
    @Test
    void testWithdraw() {
        // Given
        Account account = new Account();
        account.setAccountNumber("ACC10001");
        account.setBalance(BigDecimal.valueOf(500));

        when(accountRepository.findByAccountNumber("ACC10001")).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        // When
        Account updatedAccount = accountService.withdraw("ACC10001", BigDecimal.valueOf(200));

        // Then
        assertEquals(BigDecimal.valueOf(300), updatedAccount.getBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testWithdraw_InsufficientFunds() {
        // Given
        Account account = new Account();
        account.setAccountNumber("ACC10001");
        account.setBalance(BigDecimal.valueOf(100));

        when(accountRepository.findByAccountNumber("ACC10001")).thenReturn(Optional.of(account));

        // When & Then
        assertThrows(InsufficientFundsException.class,
                () -> accountService.withdraw("ACC10001", BigDecimal.valueOf(200)));

        verify(accountRepository, never()).save(account);
    }

    @Test
    void testDeposit_InvalidAccount() {
        // Given
        when(accountRepository.findByAccountNumber("INVALID")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> accountService.deposit("INVALID", BigDecimal.valueOf(100)));

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testCreateAccount() {
        // Given
        User user = new User();
        user.setId(1L);

        Account account = new Account();
        account.setAccountNumber("ACC10002");
        account.setAccountType(AccountType.SAVINGS);
        account.setCurrency(Currency.USD);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Account createdAccount = accountService.createAccount(1L, account);

        // Then
        assertNotNull(createdAccount);
        assertEquals("ACC10002", createdAccount.getAccountNumber());
        assertEquals(AccountType.SAVINGS, createdAccount.getAccountType());
        assertEquals(Currency.USD, createdAccount.getCurrency());

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testGetAccountDetails() {
        // Given
        Account account = new Account();
        account.setAccountNumber("ACC10003");
        account.setBalance(BigDecimal.valueOf(1000));

        when(accountRepository.findByAccountNumber("ACC10003")).thenReturn(Optional.of(account));

        // When
        Account foundAccount = accountService.getAccountByNumber("ACC10003");

        // Then
        assertNotNull(foundAccount);
        assertEquals("ACC10003", foundAccount.getAccountNumber());
        assertEquals(BigDecimal.valueOf(1000), foundAccount.getBalance());
    }
}