
/*
LocalDate.now().plusMonths(1) — aaj ki date lo aur usme 1 mahina add kar do — ye "next due date" automatically calculate ho raha hai
s.getNextDueDate().isBefore(LocalDate.now()) — check kar raha hai "kya due date aaj se pehle aa chuki hai" — matlab payment due ho chuka hai
s.getNextDueDate().isEqual(LocalDate.now()) — ya "kya due date aaj hi hai"
Subscription::isActive — ye ek method reference hai, chhota shortcut likhne ka tarika .filter(s -> s.isActive()) likhne ke bajaye — dono ka matlab same hai, bas ye chhota syntax hai
*/


package org.example.service;

import org.example.entity.Account;
import org.example.entity.Subscription;
import org.example.repository.AccountRepository;
import org.example.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Subscription addSubscription(Long accountId, Subscription subscription) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        subscription.setAccount(account);
        subscription.setStartDate(LocalDate.now());
        subscription.setNextDueDate(LocalDate.now().plusMonths(1));
        subscription.setActive(true);

        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> getSubscriptionsByAccount(Long accountId) {
        return subscriptionRepository.findAll().stream()
                .filter(s -> s.getAccount().getId().equals(accountId))
                .collect(Collectors.toList());
    }

    public List<Subscription> getDueSubscriptions(Long accountId) {
        return subscriptionRepository.findAll().stream()
                .filter(s -> s.getAccount().getId().equals(accountId))
                .filter(s -> s.getNextDueDate().isBefore(LocalDate.now()) || s.getNextDueDate().isEqual(LocalDate.now()))
                .filter(Subscription::isActive)
                .collect(Collectors.toList());
    }
}
//Poora getDueSubscriptions method ka matlab: "is account ke saare active subscriptions nikaalo jinki due date aaj ya usse pehle hai" — yehi hamara "detector" hai jo batayega kaunsa subscription payment due hai.