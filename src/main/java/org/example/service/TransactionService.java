

package org.example.service;

import org.example.entity.Account;
import org.example.entity.Transaction;
import org.example.entity.TransactionStatus;
import org.example.entity.TransactionType;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.entity.Role;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Transaction deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAccount(account);

        return transactionRepository.save(transaction);
    }
/*
account.getBalance().compareTo(amount) < 0 — BigDecimal mein hum < ya > seedha use nahi kar sakte (jaise normal numbers mein karte hain). Iske liye .compareTo() method use karte hain:
Agar result < 0 — matlab balance amount se kam hai
Agar result > 0 — matlab balance amount se zyada hai
Agar result == 0 — matlab dono barabar hain
Yahan hum check kar rahe hain "agar balance, amount se kam hai" → matlab paise kam hain → FAILED
account.getCustomer().getRole() == Role.CHILD — dekho ye kitna sundar hai: account se seedha uska customer nikal rahe hain (kyunki relationship banaya tha), aur customer ka role check kar rahe hain — ye sab automatically kaam karta hai kyunki humne pehle @ManyToOne relationship banaya tha
amount.compareTo(account.getDailyLimit()) > 0 — check kar rahe hain "agar amount, dailyLimit se zyada hai" → matlab limit cross ho rahi hai
Important logic order: pehle balance check, fir role+limit check, tabhi jaake actual paisa katega — isse hum ensure karte hain ki koi bhi galat transaction paisa na kaate
*/
    public Transaction withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAccount(account);

        if (account.getBalance().compareTo(amount) < 0) {
            transaction.setStatus(TransactionStatus.FAILED);
            return transactionRepository.save(transaction);
        }

        boolean isChild = account.getCustomer().getRole() == Role.CHILD;

        if (isChild && amount.compareTo(account.getDailyLimit()) > 0) {
            transaction.setStatus(TransactionStatus.PENDING_APPROVAL);
            return transactionRepository.save(transaction);
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        transaction.setStatus(TransactionStatus.SUCCESS);

        return transactionRepository.save(transaction);
    }

    /*
    approveTransaction — transaction dhundhta hai, uska amount ab jaake balance se katta hai (kyunki pehle withdraw ke time hum PENDING pe rakh ke paisa nahi kaate the), status SUCCESS kar deta hai
     rejectTransaction — bas status ko FAILED kar deta hai, paisa kabhi kata hi nahi tha to katne ka sawal nahi
    */
    public Transaction approveTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Account account = transaction.getAccount();
        account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        accountRepository.save(account);

        transaction.setStatus(TransactionStatus.SUCCESS);
        return transactionRepository.save(transaction);
    }

    public Transaction rejectTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setStatus(TransactionStatus.FAILED);
        return transactionRepository.save(transaction);
    }
    public java.util.List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public java.util.List<Transaction> getTransactionsByAccount(Long accountId) {
        return transactionRepository.findAll().stream()
                .filter(t -> t.getAccount().getId().equals(accountId))
                .collect(java.util.stream.Collectors.toList());
    }
}