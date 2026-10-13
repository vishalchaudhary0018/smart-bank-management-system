package org.example.service;

import org.example.dto.SchemeSuggestion;
import org.example.entity.Customer;
import org.example.entity.CustomerProfile;
import org.example.entity.Scheme;
import org.example.repository.CustomerProfileRepository;
import org.example.repository.CustomerRepository;
import org.example.repository.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class SchemeService {

    @Autowired
    private SchemeRepository schemeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerProfileRepository profileRepository;

    public List<SchemeSuggestion> getSuggestions(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        CustomerProfile profile = profileRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new RuntimeException("Pehle apni profile complete karo"));

        int age = Period.between(profile.getDateOfBirth(), LocalDate.now()).getYears();

        List<Scheme> schemes = schemeRepository.findEligible(
                age, profile.getAnnualIncome(), profile.getGender());

        List<SchemeSuggestion> result = new ArrayList<>();
        for (Scheme scheme : schemes) {
            SchemeSuggestion suggestion = new SchemeSuggestion();
            suggestion.setId(scheme.getId());
            suggestion.setName(scheme.getName());
            suggestion.setDescription(scheme.getDescription());
            suggestion.setOfficialLink(scheme.getOfficialLink());
            suggestion.setNewlyAdded(scheme.getCreatedAt() != null
                    && scheme.getCreatedAt().isAfter(LocalDate.now().minusDays(30)));
            result.add(suggestion);
        }
        return result;
    }
}