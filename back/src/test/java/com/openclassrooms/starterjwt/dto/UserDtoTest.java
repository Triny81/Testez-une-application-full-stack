package com.openclassrooms.starterjwt.dto;

import lombok.var;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    private final Validator validator;

    public UserDtoTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void shouldSetAndGetValues() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        userDto.setLastName("Doe");
        userDto.setFirstName("John");
        userDto.setAdmin(true);
        userDto.setPassword("password123");
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());

        assertEquals(1L, userDto.getId());
        assertEquals("test@example.com", userDto.getEmail());
        assertEquals("Doe", userDto.getLastName());
        assertEquals("John", userDto.getFirstName());
        assertTrue(userDto.isAdmin());
        assertEquals("password123", userDto.getPassword());
        assertNotNull(userDto.getCreatedAt());
        assertNotNull(userDto.getUpdatedAt());
    }

    @Test
    void shouldValidateAnnotations() {
        UserDto userDto = new UserDto(null, "invalid-email", "", "", false, "", null, null);

        var violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldTestConstructorsAndSetters() {
        UserDto userDto = new UserDto(1L, "test@example.com", "Doe", "John", true, "password", LocalDateTime.now(), LocalDateTime.now());

        assertEquals(1L, userDto.getId());
        assertEquals("test@example.com", userDto.getEmail());
        assertEquals("Doe", userDto.getLastName());
        assertEquals("John", userDto.getFirstName());
        assertTrue(userDto.isAdmin());

        // Test setters
        userDto.setId(2L);
        userDto.setEmail("new@example.com");
        userDto.setLastName("Smith");
        userDto.setFirstName("Jane");
        userDto.setAdmin(false);

        assertEquals(2L, userDto.getId());
        assertEquals("new@example.com", userDto.getEmail());
        assertEquals("Smith", userDto.getLastName());
        assertEquals("Jane", userDto.getFirstName());
        assertFalse(userDto.isAdmin());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();

        UserDto user1 = new UserDto(1L, "test@example.com", "Doe", "John", true, "password", now, now);
        UserDto user2 = new UserDto(1L, "test@example.com", "Doe", "John", true, "password", now, now);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());

        user2.setId(2L);
        assertNotEquals(user1, user2);
    }

    @Test
    void shouldTestToString() {
        UserDto userDto = new UserDto(1L, "test@example.com", "Doe", "John", true, "password", LocalDateTime.now(), LocalDateTime.now());

        String toString = userDto.toString();
        assertTrue(toString.contains("test@example.com"));
        assertTrue(toString.contains("Doe"));
        assertTrue(toString.contains("John"));
    }
}
