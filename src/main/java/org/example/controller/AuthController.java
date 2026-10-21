
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
