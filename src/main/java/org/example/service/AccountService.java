/*
* customerRepository.findById(customerId) — ye database mein customerId se customer dhundhta hai. Lekin ye seedha Customer return nahi karta — ye ek special wrapper return karta hai jise Optional kehte hain
Optional kya hai (simple mein): socho tum kisi cheez ko dhundh rahe ho jo mil bhi sakti hai, na bhi mile — jaise agar galat customerId diya ho to customer milega hi nahi. Optional ek safety box hai jo bolta hai "isme cheez ho bhi sakti hai, khaali bhi ho sakta hai — pehle check karo"
.orElseThrow(() -> new RuntimeException("Customer not found")) — iska matlab hai: "agar customer mil gaya to use nikaal ke do, agar nahi mila to ek error throw kar do" — isse hum crash hone se bachte hain aur proper error message milta hai
account.setCustomer(customer) — dhundha hua customer, account ke saath link kar rahe hain
account.setBalance(BigDecimal.ZERO) — naya account hamesha 0 balance se start hota hai, ye humari business rule hai
.save(account) — ab poori tarah taiyar account ko database mein save kar rahe hain
*
* */

package org.example.service;

import org.example.entity.Account;
import org.example.entity.Customer;
import org.example.repository.AccountRepository;
import org.example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Account createAccount(Long customerId, Account account) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        account.setCustomer(customer);
        account.setBalance(BigDecimal.ZERO);

        return accountRepository.save(account);
    }
    public java.util.List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
}