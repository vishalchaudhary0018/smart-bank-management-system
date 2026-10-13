/*
LoginRequest — ek simple DTO (Data Transfer Object), jisse humein sirf email-password chahiye, poora Customer object nahi
.filter(c -> c.getEmail().equals(...)).findFirst() — saare customers mein se email match karne wala dhundh raha hai
passwordEncoder.matches(plainPassword, encryptedPassword) — ye check karta hai "jo password type kiya (plain), kya wo encrypted wale se match karta hai" — encrypt karke compare nahi karte, ye method khud handle karta hai
jwtUtil.generateToken(...) — agar sab sahi hai, to token generate karke return kar deta hai
*/

package org.example.controller;

import org.example.dto.LoginRequest;
import org.example.entity.Customer;
import org.example.repository.CustomerRepository;
import org.example.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        Customer customer = customerRepository.findAll().stream()
                .filter(c -> c.getEmail().equals(loginRequest.getEmail()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), customer.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return jwtUtil.generateToken(customer.getEmail());
    }
}
