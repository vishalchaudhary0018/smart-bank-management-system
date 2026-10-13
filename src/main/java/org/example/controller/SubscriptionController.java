package org.example.controller;

import org.example.entity.Subscription;
import org.example.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/add/{accountId}")
    public Subscription addSubscription(@PathVariable Long accountId, @RequestBody Subscription subscription) {
        return subscriptionService.addSubscription(accountId, subscription);
    }

    @GetMapping("/account/{accountId}")
    public List<Subscription> getByAccount(@PathVariable Long accountId) {
        return subscriptionService.getSubscriptionsByAccount(accountId);
    }

    @GetMapping("/due/{accountId}")
    public List<Subscription> getDue(@PathVariable Long accountId) {
        return subscriptionService.getDueSubscriptions(accountId);
    }
}
