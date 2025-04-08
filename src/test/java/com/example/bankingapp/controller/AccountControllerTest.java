package com.example.bankingapp.controller;

import com.example.bankingapp.config.JwtTokenProvider;
import com.example.bankingapp.entity.Account;
import com.example.bankingapp.entity.Role;
import com.example.bankingapp.entity.User;
import com.example.bankingapp.entity.enums.AccountType;
import com.example.bankingapp.entity.enums.Currency;
import com.example.bankingapp.entity.enums.RoleName;
import com.example.bankingapp.repository.RoleRepository;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.service.AccountService;
import com.example.bankingapp.service.impl.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    private User testUser;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AccountService accountService;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        // Ensure role exists
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(RoleName.ROLE_USER);
                    return roleRepository.save(role);
                });

        // Create test user and capture it
        User user = new User();
        user.setUsername("john_doe");
        user.setFullName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRoles(Set.of(userRole));

        testUser = userRepository.save(user); // ✅ Capture the saved user

        // Generate JWT token
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("john_doe");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        jwtToken = jwtTokenProvider.generateToken(authentication);
    }

    @Test
    void shouldReturnUnauthorizedWhenCreatingAccountWithoutToken() throws Exception {
        Account account = getTestAccount();

        mockMvc.perform(post("/api/accounts/create/"+testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldCreateAccountWithToken() throws Exception {
        Account account = getTestAccount();

        mockMvc.perform(post("/api/accounts/create/" + testUser.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber", is(account.getAccountNumber())))
                .andExpect(jsonPath("$.currency", is(account.getCurrency().name())))
                .andExpect(jsonPath("$.accountType", is(account.getAccountType().name())));
    }

    @Test
    void shouldDepositToAccount() throws Exception {
        // Ensure account exists
        accountService.createAccount(testUser.getId(), getTestAccount());

        mockMvc.perform(post("/api/accounts/ACC1000"+testUser.getId()+"/deposit")
                        .header("Authorization", "Bearer " + jwtToken)
                        .param("amount", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber", is("ACC1000"+testUser.getId())));
    }

    @Test
    void shouldWithdrawFromAccount() throws Exception {
        // Ensure account exists and has balance
        Account account = accountService.createAccount(testUser.getId(), getTestAccount());
        account.setBalance(BigDecimal.valueOf(2000));
        accountService.deposit(account.getAccountNumber(), BigDecimal.valueOf(2000));

        mockMvc.perform(post("/api/accounts/ACC1000"+testUser.getId()+"/withdraw")
                        .header("Authorization", "Bearer " + jwtToken)
                        .param("amount", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber", is("ACC1000"+testUser.getId())));
    }

    @Test
    void shouldGetAccountDetails() throws Exception {
        Account account = accountService.createAccount(testUser.getId(), getTestAccount());

        mockMvc.perform(get("/api/accounts/" + account.getAccountNumber())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber", is(account.getAccountNumber())))
                .andExpect(jsonPath("$.accountType", is(account.getAccountType().name())))
                .andExpect(jsonPath("$.currency", is(account.getCurrency().name())));
    }


    // Utility method to create a test account object
    private Account getTestAccount() {
        Account account = new Account();
        account.setAccountNumber("ACC1000"+testUser.getId());
        account.setAccountType(AccountType.SAVINGS);
        account.setCurrency(Currency.USD);
        account.setInterestRate(BigDecimal.valueOf(3.5));
        return account;
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}
