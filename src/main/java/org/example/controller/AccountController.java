
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