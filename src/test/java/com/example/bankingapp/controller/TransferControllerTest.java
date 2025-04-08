package com.example.bankingapp.controller;

import com.example.bankingapp.config.JwtTokenProvider;
import com.example.bankingapp.dto.TransferRequest;
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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private String jwtToken;
    private User testUser;
    private Account sourceAccount;
    private Account destinationAccount;

    @BeforeEach
    void setUp() {
        // Ensure role exists
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(RoleName.ROLE_USER);
                    return roleRepository.save(role);
                });

        // Create test user
        User user = new User();
        user.setUsername("transfer_user");
        user.setFullName("Transfer User");
        user.setEmail("transfer@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRoles(Set.of(userRole));

        testUser = userRepository.save(user);

        // Create source and destination accounts
        sourceAccount = accountService.createAccount(testUser.getId(), createTestAccount("ACC20001"));
        destinationAccount = accountService.createAccount(testUser.getId(), createTestAccount("ACC20002"));

        // Deposit initial balance to source account
        accountService.deposit(sourceAccount.getAccountNumber(), BigDecimal.valueOf(5000));

        // Generate JWT token
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("transfer_user");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        jwtToken = jwtTokenProvider.generateToken(authentication);
    }

    @Test
    void shouldFailTransferDueToSourceAccountNotFound() throws Exception {
        // Prepare TransferRequest with invalid source account
        TransferRequest request = new TransferRequest();
        request.setFromAccount("INVALID_ACC");
        request.setToAccount(destinationAccount.getAccountNumber());
        request.setAmount(BigDecimal.valueOf(1000));

        mockMvc.perform(post("/api/transfers")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Account not found")));
    }

    @Test
    void shouldFailTransferDueToDestinationAccountNotFound() throws Exception {
        // Prepare TransferRequest with invalid destination account
        TransferRequest request = new TransferRequest();
        request.setFromAccount(sourceAccount.getAccountNumber());
        request.setToAccount("INVALID_ACC");
        request.setAmount(BigDecimal.valueOf(1000));

        mockMvc.perform(post("/api/transfers")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Account not found")));
    }

    // Utility method to create a test account
    private Account createTestAccount(String accountNumber) {
        Account account = new Account();
        account.setAccountNumber(accountNumber);
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
