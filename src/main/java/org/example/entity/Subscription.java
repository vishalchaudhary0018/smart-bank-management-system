//Ye feature transaction history mein pattern dhundhega — agar same amount ka transaction same account se, har mahine repeat ho raha hai, to use "subscription" maan lenge.

/*

subscriptionName — jaise "Netflix", "Gym Membership"
amount — monthly kitna katega
startDate, nextDueDate — kab shuru hua, agli baar kab katega — LocalDate use kiya kyunki sirf date chahiye, time nahi
boolean active — customer chahe to subscription band (inactive) kar sake bina delete kiye
@ManyToOne private Account account; — same relationship pattern jo pehle sikha tha — ek account ke multiple subscriptions ho sakte hain

Type karo, save karo, bolo "ho gaya" — fir Repository banayenge (ekdum chhota, 2 minute ka kaam, tumhe ab pattern pata hai).



*/



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