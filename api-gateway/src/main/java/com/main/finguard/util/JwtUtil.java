package com.main.finguard.util;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private static final Dotenv dotenv = Dotenv.load();
	private static final String SECRET_KEY = dotenv.get("SECRET_KEY");

	private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

	// Parse and validate the JWT token
	public static Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	private static Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// Check if the token is expired
	public static boolean isTokenExpired(Claims claims) {
		return claims.getExpiration().before(new Date());
	}

	// Extract username (subject) from token
	public static String getUsername(Claims claims) {
		return claims.getSubject();
	}

	// Extract roles from token
	public static String getRoles(Claims claims) {
		return claims.get("role", String.class);
	}
}
