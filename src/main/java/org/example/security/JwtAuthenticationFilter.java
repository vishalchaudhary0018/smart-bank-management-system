/*
OncePerRequestFilter — ye ek special Spring class hai jo guarantee karti hai har request pe exactly ek baar check ho (naam se hi samajh aata hai)
request.getHeader("Authorization") — request ke header mein se token nikaal raha hai. Real duniya mein, jab tum login karte ho koi app mein, wo app har request ke saath apne header mein token bhejता hai — ye normal practice hai
authHeader.startsWith("Bearer ") — token hamesha "Bearer " word ke saath bheja jaata hai (ye ek standard convention hai), isliye check kar rahe hain wo format sahi hai
token.substring(7) — "Bearer " word (jo 7 characters ka hai) hata ke, sirf actual token nikaal rahe hain
SecurityContextHolder.getContext().setAuthentication(authToken) — ye batata hai Spring ko "ye request authenticated hai, isko allow karo aage jaane"
filterChain.doFilter(request, response) — chahe token valid ho ya na ho, ye line hamesha chalti hai — isse request aage badhती hai (agar authenticate nahi hui, to aage security khud reject kar degi)
*/

package org.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.isTokenValid(token)) {
                String email = jwtUtil.extractEmail(token);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
