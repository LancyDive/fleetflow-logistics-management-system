package com.lancydive.fleetflow.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.lancydive.fleetflow.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.access-token-expiration}")
	private long accessTokenExpiration;
	
	@Value("${jwt.refresh-token-expiration}")
	private long refreshTokenExpiration;
	
	private SecretKey getSigningKey() {
	    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}
	
	public String generateAccessToken(User user) {
		return Jwts.builder()
				.subject(user.getEmail())
				.claim("role", user.getRole().name())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis()+accessTokenExpiration))
				.signWith(getSigningKey())
				.compact();
				
	}

	public String generateRefreshToken(User user) {
		return Jwts.builder()
				.subject(user.getEmail())
				.claim("role", user.getRole().name())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis()+refreshTokenExpiration))
				.signWith(getSigningKey())
				.compact();
	}
	
	private Claims extractAllClaims(String token) {

	    return Jwts.parser()
	            .verifyWith(getSigningKey())
	            .build()
	            .parseSignedClaims(token)
	            .getPayload();
	}
	
	public String extractUsername (String token) {
		return extractAllClaims(token).getSubject();
	}

	public Date extractExpiration (String token) {
		Claims claims = extractAllClaims(token);
		return claims.getExpiration();
	}
	
	private boolean isTokenExpired(String token) {
	    return extractExpiration(token).before(new Date());
	}
	
	public boolean isTokenValid (String token,UserDetails userDetails) {
		String username = extractUsername(token);
		
		return username.equals(userDetails.getUsername()) &&
				!isTokenExpired(token);
	}
}