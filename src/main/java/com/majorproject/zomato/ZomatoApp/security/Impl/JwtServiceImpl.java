package com.majorproject.zomato.ZomatoApp.security.Impl;

import com.majorproject.zomato.ZomatoApp.entity.UserEntity;
import com.majorproject.zomato.ZomatoApp.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.key}")
    private String key;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAccessToken(UserEntity user) {

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email" , user.getEmail())
                .claim("role" , user.getRole().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10))
                .signWith(getKey())
                .compact();

    }

    @Override
    public String generateRefreshToken(UserEntity user) {

        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L*60*60*24*30*6))
                .signWith(getKey())
                .compact();
    }

    @Override
    public Long getUserIdFromToken(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        //extract userId
        return Long.valueOf(claims.getSubject());
    }
}
