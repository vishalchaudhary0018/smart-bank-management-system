/*
* @PostMapping("/create/{customerId}") — dekho yahan URL ke andar {customerId} hai — ye ek placeholder hai. Matlab jab request aayegi /api/accounts/create/1 (jahan 1 kisi bhi customer ki id ho sakti hai), to Spring us 1 ko pakad ke method mein de dega
@PathVariable Long customerId — ye batata hai "URL mein jo {customerId} hai, usko is variable mein daal do"*/

package org.example.controller;

import org.example.entity.Account;
import org.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create/{customerId}")
    public Account createAccount(@PathVariable Long customerId, @RequestBody Account account) {
        return accountService.createAccount(customerId, account);
    }
    @GetMapping
    public java.util.List<Account> getAll() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Account getById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }
}