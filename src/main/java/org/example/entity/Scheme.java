package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "schemes")
@Data
public class Scheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 500)
    private String description;

    private int minAge;

    private int maxAge;

    private BigDecimal maxIncome;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String officialLink;

    private LocalDate createdAt;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }
    }
}