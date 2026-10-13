/*
@RequestBody BigDecimal amount — is baar body mein hum poora object nahi bhej rahe, sirf ek number bhej rahe hain (amount kitna deposit karna hai)
*/
package org.example.controller;

import org.example.entity.Transaction;
import org.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit/{accountId}")
    public Transaction deposit(@PathVariable Long accountId, @RequestBody BigDecimal amount) {
        return transactionService.deposit(accountId, amount);
    }
    @PostMapping("/withdraw/{accountId}")
    public Transaction withdraw(@PathVariable Long accountId, @RequestBody BigDecimal amount) {
        return transactionService.withdraw(accountId, amount);
    }
    /*
    Naya concept — PutMapping kyun

Ab tak humne sirf @PostMapping use kiya tha (naya data create karne ke liye). Ye do methods naya data create nahi kar rahe, balki existing transaction ko update kar rahe hain (status change ho raha hai) — isliye HTTP convention mein iske liye @PutMapping use karte hain.

Simple rule yaad rakho:

POST = naya kuch banao (create)
PUT = jo already hai use badlo (update)
GET = data lekar aao (read) — ye humne abhi tak use nahi kiya, jald hi karenge
    */
    @PutMapping("/approve/{transactionId}")
    public Transaction approve(@PathVariable Long transactionId) {
        return transactionService.approveTransaction(transactionId);
    }

    @PutMapping("/reject/{transactionId}")
    public Transaction reject(@PathVariable Long transactionId) {
        return transactionService.rejectTransaction(transactionId);
    }
    @GetMapping
    public java.util.List<Transaction> getAll() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/account/{accountId}")
    public java.util.List<Transaction> getByAccount(@PathVariable Long accountId) {
        return transactionService.getTransactionsByAccount(accountId);
    }
}