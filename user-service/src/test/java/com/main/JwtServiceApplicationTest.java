package com.main;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.SignatureException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.main.service.JwtService;

@SpringBootTest
public class JwtServiceApplicationTest {

	 @Test
	    void testGenerateToken() {
	        JwtService jwtService = new JwtService();
	        String userName = "testUser";
	        String role = "admin";

	        String token = jwtService.generateToken(userName, role);

	        assertNotNull(token);
	        assertTrue(token.startsWith("eyJ")); // JWT tokens typically start with "eyJ"
	    }

	    @Test
	    void testValidateToken_InvalidToken() {
	        JwtService jwtService = new JwtService();
	        String invalidToken = "invalid.token.value";

	        assertThrows(io.jsonwebtoken.MalformedJwtException.class, () -> {
	            jwtService.validateToken(invalidToken);
	        });
	    }

	    @Test
	    void testValidateToken_ValidToken() {
	        JwtService jwtService = new JwtService();
	        String userName = "testUser";
	        String role = "admin";
	        String token = jwtService.generateToken(userName, role);

	        assertDoesNotThrow(() -> jwtService.validateToken(token));
	    }
}
