package com.benconsult.user_management_api.users.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtUtilService {
    String generateToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    boolean validateToken(String token, UserDetails userDetails);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String extractUsername(String token);
}
