package org.example.dto;

import lombok.Data;
import org.example.entity.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProfileDto {
    private LocalDate dateOfBirth;
    private BigDecimal annualIncome;
    private Gender gender;
}
