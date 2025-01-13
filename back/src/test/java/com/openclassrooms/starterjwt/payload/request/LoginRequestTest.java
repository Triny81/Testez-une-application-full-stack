package com.openclassrooms.starterjwt.payload.request;

import lombok.var;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private final Validator validator;

    public LoginRequestTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void shouldTestGettersAndSetters() {
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        assertEquals("test@example.com", loginRequest.getEmail());
        assertEquals("password123", loginRequest.getPassword());
    }

    @Test
    void shouldValidateConstraints() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("");
        loginRequest.setPassword("");


        var violations = validator.validate(loginRequest);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassValidation() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        var violations = validator.validate(loginRequest);

        assertTrue(violations.isEmpty());
    }
}
