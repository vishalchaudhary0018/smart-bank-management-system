/*
csrf.disable() — CSRF ek security feature hai jo normally websites ke liye zaroori hota hai, lekin APIs ke liye disable karna standard practice hai
.anyRequest().permitAll() — abhi ke liye maine sab kuch open rakha hai (koi bhi bina token ke access kar sakta hai) — isse pehle hum confirm kar lein ki login kaam kar raha hai, uske baad hum actual restriction (token check) add karenge next step mein
.headers(...frameOptions...disable()) — ye H2 console ko kaam karne dene ke liye zaroori hai (Spring Security H2 console ko block kar deta hai warna)
-----------------------------------------------------------------------------------------------
.anyRequest().permitAll() ko badal ke .anyRequest().authenticated() kar diya — ab baaki saari APIs ko token chahiye hoga, sirf login/register/H2-console free hain
.addFilterBefore(jwtAuthenticationFilter, ...) — ye humare naye filter ko security chain mein jodता hai, batata hai "har request pehle isse guzro"
*/
package org.example.config;

import org.example.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/customers/register", "/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}