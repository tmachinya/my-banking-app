package com.example.bankingapp.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Aspect
@Component
public class GLAccountAspect {

    // ✅ Pointcut for deposit method
    @Pointcut("execution(* com.example.bankingapp.service.AccountService.deposit(..))")
    public void depositOperation() {}

    // ✅ Pointcut for withdraw method
    @Pointcut("execution(* com.example.bankingapp.service.AccountService.withdraw(..))")
    public void withdrawOperation() {}

    // ✅ Pointcut for transfer method
    @Pointcut("execution(* com.example.bankingapp.service.TransferService.transferAmount(..))")
    public void transferOperation() {}

    // ✅ After deposit
    @AfterReturning(pointcut = "depositOperation()", returning = "result")
    public void logDeposit(JoinPoint joinPoint, Object result) {
        String accountNumber = (String) joinPoint.getArgs()[0];
        BigDecimal amount = (BigDecimal) joinPoint.getArgs()[1];

        log.info("[GL Update] Deposit - Account: {}, Amount: {}", accountNumber, amount);
    }

    // ✅ After withdraw
    @AfterReturning(pointcut = "withdrawOperation()", returning = "result")
    public void logWithdraw(JoinPoint joinPoint, Object result) {
        String accountNumber = (String) joinPoint.getArgs()[0];
        BigDecimal amount = (BigDecimal) joinPoint.getArgs()[1];

        log.info("[GL Update] Withdrawal - Account: {}, Amount: {}", accountNumber, amount);
    }

    // ✅ After transfer
    @AfterReturning(pointcut = "transferOperation()", returning = "result")
    public void logTransfer(JoinPoint joinPoint, Object result) {
        String fromAccount = (String) joinPoint.getArgs()[0];
        String toAccount = (String) joinPoint.getArgs()[1];
        BigDecimal amount = (BigDecimal) joinPoint.getArgs()[2];

        log.info("[GL Update] Transfer - From: {}, To: {}, Amount: {}", fromAccount, toAccount, amount);
    }
}