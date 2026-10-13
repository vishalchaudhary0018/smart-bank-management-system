package org.example.config;

import org.example.entity.Gender;
import org.example.entity.Scheme;
import org.example.repository.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SchemeDataLoader implements CommandLineRunner {

    @Autowired
    private SchemeRepository schemeRepository;

    @Override
    public void run(String... args) {
        if (schemeRepository.count() > 0) {
            return;
        }

        schemeRepository.save(createScheme("Atal Pension Yojana",
                "Retirement ke baad pension ke liye scheme", 18, 40, null, Gender.ANY,
                "https://www.jansuraksha.gov.in"));

        schemeRepository.save(createScheme("PM Jeevan Jyoti Bima Yojana",
                "Life insurance cover", 18, 50, null, Gender.ANY,
                "https://www.jansuraksha.gov.in"));

        schemeRepository.save(createScheme("PM Suraksha Bima Yojana",
                "Accident insurance cover", 18, 70, null, Gender.ANY,
                "https://www.jansuraksha.gov.in"));

        schemeRepository.save(createScheme("Sukanya Samriddhi Yojana",
                "Ladki ki padhai aur shaadi ke liye savings scheme", 0, 10, null, Gender.FEMALE,
                "https://www.indiapost.gov.in"));

        schemeRepository.save(createScheme("Student Education Loan Support (sample)",
                "Education loan se jude sample scheme, income limit ke saath", 18, 35,
                new BigDecimal("450000"), Gender.ANY,
                "https://www.vidyalakshmi.co.in"));
    }

    private Scheme createScheme(String name, String description, int minAge, int maxAge,
                                BigDecimal maxIncome, Gender gender, String officialLink) {
        Scheme scheme = new Scheme();
        scheme.setName(name);
        scheme.setDescription(description);
        scheme.setMinAge(minAge);
        scheme.setMaxAge(maxAge);
        scheme.setMaxIncome(maxIncome);
        scheme.setGender(gender);
        scheme.setOfficialLink(officialLink);
        return scheme;
    }
}