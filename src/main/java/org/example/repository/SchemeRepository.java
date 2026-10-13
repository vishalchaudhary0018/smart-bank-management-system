package org.example.repository;

import org.example.entity.Gender;
import org.example.entity.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SchemeRepository extends JpaRepository<Scheme, Long> {

    @Query("SELECT s FROM Scheme s " +
            "WHERE :age BETWEEN s.minAge AND s.maxAge " +
            "AND (s.maxIncome IS NULL OR :income <= s.maxIncome) " +
            "AND (s.gender = org.example.entity.Gender.ANY OR s.gender = :gender)")
    List<Scheme> findEligible(@Param("age") int age,
                              @Param("income") BigDecimal income,
                              @Param("gender") Gender gender);
}
