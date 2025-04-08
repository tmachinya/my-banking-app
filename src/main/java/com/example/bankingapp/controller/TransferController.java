package com.example.bankingapp.controller;

import com.example.bankingapp.dto.TransferRequest;
import com.example.bankingapp.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        transferService.transferAmount(request.getFromAccount(), request.getToAccount(), request.getAmount());
        return ResponseEntity.ok("Transfer successful");
    }
}
