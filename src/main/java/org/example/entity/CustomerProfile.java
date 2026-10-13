package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "customer_profiles")
@Data
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id", unique = true)
    private Customer customer;

    private LocalDate dateOfBirth;

    private BigDecimal annualIncome;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
