/*
@Component — @Service jaisa hi hai, bas ye batata hai "ye ek utility/helper class hai" jise Spring manage karega
SecretKey secretKey — ye ek secret key hai jo token ko encrypt karne ke liye use hoti hai — isko koi bahar wala nahi jaanta, isi se pata chalta hai token genuine hai ya fake
generateToken(String email) — customer ka email leke, ek token banata hai jisme email chhupa hota hai, plus ek expiry time (1 ghanta baad token khud invalid ho jayega — security ke liye zaroori hai)
extractEmail(String token) — token ke andar se email wapas nikaal ta hai (verify karne ke liye ki ye kiska token hai)
isTokenValid(String token) — check karta hai token genuine hai aur expire nahi hua — agar koi chhed-chhad ki ho token mein, to ye false return karega
*/

package org.example.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationMs = 3600000; // 1 hour

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}