package com.moviebookingapp.movie_booking_app.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  private final Key SECRET_KEY =
      Keys.hmacShaKeyFor("movie_app_secretmovie_app_secretmovie_app_secret".getBytes());

  public String generateToken(String loginId, String role) {
    return Jwts.builder()
        .setSubject(loginId)
        .claim("role", role)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000)) // 1 year
        .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractUsername(String token) {
    return parseToken(token).getBody().getSubject();
  }

  public String extractRole(String token) {
    return parseToken(token).getBody().get("role", String.class);
  }

  public boolean validateToken(String token) {
    try {
      parseToken(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  private Jws<Claims> parseToken(String token) {
    return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
  }
}
