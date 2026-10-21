



package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subscriptionName;

    private BigDecimal amount;

    private LocalDate startDate;

    private LocalDate nextDueDate;

    private boolean active;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}