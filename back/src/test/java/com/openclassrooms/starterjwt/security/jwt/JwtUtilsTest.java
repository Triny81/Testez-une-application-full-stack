package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtils = new JwtUtils();

        Field jwtSecretField = JwtUtils.class.getDeclaredField("jwtSecret");
        jwtSecretField.setAccessible(true);
        jwtSecretField.set(jwtUtils, "test-secret");

        Field jwtExpirationMsField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
        jwtExpirationMsField.setAccessible(true);
        jwtExpirationMsField.set(jwtUtils, 3600000); // 1 hour
    }

    @Test
    void shouldGenerateAndValidateJwtToken() {
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = UserDetailsImpl.builder().username("testUser").build();
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);
        boolean isValid = jwtUtils.validateJwtToken(token);

        assertNotNull(token);
        assertTrue(isValid);
        assertEquals("testUser", jwtUtils.getUserNameFromJwtToken(token));
    }

    @Test
    void shouldReturnFalseForInvalidJwtToken() {
        String invalidToken = "invalid-token";

        boolean isValid = jwtUtils.validateJwtToken(invalidToken);
        assertFalse(isValid);
    }

    @Test
    void shouldHandleExpiredJwtToken() {
        String expiredToken = "";

        boolean isValid = jwtUtils.validateJwtToken(expiredToken);
        assertFalse(isValid);
    }
}

