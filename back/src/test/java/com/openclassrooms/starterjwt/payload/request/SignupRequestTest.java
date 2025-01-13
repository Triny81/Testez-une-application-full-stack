package com.openclassrooms.starterjwt.payload.request;

import lombok.var;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {

    private final Validator validator;

    public SignupRequestTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void shouldTestGettersAndSetters() {
        SignupRequest signupRequest = new SignupRequest();

        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        assertEquals("test@example.com", signupRequest.getEmail());
        assertEquals("John", signupRequest.getFirstName());
        assertEquals("Doe", signupRequest.getLastName());
        assertEquals("password123", signupRequest.getPassword());
    }

    @Test
    void shouldValidateConstraints() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("invalid-email");
        signupRequest.setFirstName("Jo");
        signupRequest.setLastName("D");
        signupRequest.setPassword("123");

        var violations = validator.validate(signupRequest);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassValidation() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        var violations = validator.validate(signupRequest);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("password123");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password123");

        SignupRequest request3 = new SignupRequest();
        request3.setEmail("different@example.com");
        request3.setFirstName("Jane");
        request3.setLastName("Smith");
        request3.setPassword("differentPassword");

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void shouldTestToString() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        String result = request.toString();


        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
    }

    @Test
    void shouldTestCanEqual() {
        SignupRequest request1 = new SignupRequest();
        SignupRequest request2 = new SignupRequest();

        assertTrue(request1.canEqual(request2));
        assertFalse(request1.canEqual("Some other object"));
    }

    @Test
    void shouldTestSettersAndGetters() {
        SignupRequest request = new SignupRequest();

        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        assertEquals("test@example.com", request.getEmail());
        assertEquals("John", request.getFirstName());
        assertEquals("Doe", request.getLastName());
        assertEquals("password123", request.getPassword());
    }
}
