package com.collapp.project.security;

import com.collapp.project.entity.User;
import com.collapp.project.entity.enums.SystemRole;
import com.collapp.project.util.TestObjectFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private JwtService jwtService;
    private CustomUserDetails userDetails;
    private static final String SECRET_KEY = "mysecretkeymustbe32byteslong!!ex";
    private static final long EXPIRATION = 3600000;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION);

        User user = TestObjectFactory.createUser();
        userDetails = new CustomUserDetails(user);
    }

    @Test
    void generateAndExtractEmail() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        String extractedEmail = jwtService.extractEmail(token);
        assertEquals(userDetails.getUser().getEmail(), extractedEmail);
    }

    @Test
    void isTokenValid_correctUser() {
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_wrongUser() {
        String token = jwtService.generateToken(userDetails);

        User wrongUser = TestObjectFactory.createUser2();
        CustomUserDetails wrongUserDetails = new CustomUserDetails(wrongUser);

        boolean isValid = jwtService.isTokenValid(token, wrongUserDetails);

        assertFalse(isValid);
    }

    @Test
    void tokenGeneration_includesRole() {
        String token = jwtService.generateToken(userDetails);
        String email = jwtService.extractEmail(token);

        assertEquals(userDetails.getUser().getEmail(), email);
        assertEquals(SystemRole.ROLE_CREATIVE, userDetails.getUser().getSystemRole());
    }
}
