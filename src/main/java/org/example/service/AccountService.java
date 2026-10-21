

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