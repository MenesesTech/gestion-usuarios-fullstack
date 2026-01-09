package com.benconsult.user_management_api.users.service.impl;

import com.benconsult.user_management_api.users.service.JwtUtilService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtUtilServiceImpl implements JwtUtilService {

    private static final String JWT_SECRET_KEY = "TExBVkVfTVVZX1NFQ1JFVEzE3Zmxu7BSGSJx72BSBXM";
    private static final long JWT_TIME_VALIDITY = 1000 * 60  * 15;
    private static final long JWT_TIME_REFRESH_VALIDATE = 1000 * 60  * 60 * 24;

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() + JWT_TIME_VALIDITY)
                )
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() + JWT_TIME_REFRESH_VALIDATE)
                )
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        return extractClaim(token, Claims::getSubject).equals(userDetails.getUsername())
                && !extractClaim(token, Claims::getExpiration).before(new Date());
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET_KEY).build().parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
}
