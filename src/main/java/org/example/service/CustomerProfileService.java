package org.example.service;

import org.example.dto.ProfileDto;
import org.example.entity.Customer;
import org.example.entity.CustomerProfile;
import org.example.entity.Gender;
import org.example.repository.CustomerProfileRepository;
import org.example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CustomerProfileService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerProfileRepository profileRepository;

    public ProfileDto saveProfile(String email, ProfileDto dto) {
        if (dto.getDateOfBirth() == null || dto.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new RuntimeException("Date of birth sahi nahi hai");
        }
        if (dto.getAnnualIncome() == null || dto.getAnnualIncome().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Annual income sahi nahi hai");
        }
        if (dto.getGender() == null || dto.getGender() == Gender.ANY) {
            throw new RuntimeException("Gender MALE ya FEMALE hona chahiye");
        }

        Customer customer = findCustomer(email);

        CustomerProfile profile = profileRepository.findByCustomerId(customer.getId())
                .orElse(new CustomerProfile());
        profile.setCustomer(customer);
        profile.setDateOfBirth(dto.getDateOfBirth());
        profile.setAnnualIncome(dto.getAnnualIncome());
        profile.setGender(dto.getGender());
        profileRepository.save(profile);

        return toDto(profile);
    }

    public ProfileDto getProfile(String email) {
        Customer customer = findCustomer(email);
        CustomerProfile profile = profileRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new RuntimeException("Pehle apni profile complete karo"));
        return toDto(profile);
    }

    private Customer findCustomer(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    private ProfileDto toDto(CustomerProfile profile) {
        ProfileDto dto = new ProfileDto();
        dto.setDateOfBirth(profile.getDateOfBirth());
        dto.setAnnualIncome(profile.getAnnualIncome());
        dto.setGender(profile.getGender());
        return dto;
    }
}