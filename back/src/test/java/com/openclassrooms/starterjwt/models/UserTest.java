package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldSetAndGetValues() {
        User user = new User()
                .setId(1L)
                .setEmail("test@example.com")
                .setLastName("Doe")
                .setFirstName("John")
                .setPassword("password123")
                .setAdmin(true)
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isAdmin());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void shouldValidateEqualsAndHashCode() {
        User user1 = new User(1L, "test@example.com", "Doe", "John", "password123", true, LocalDateTime.now(),
                LocalDateTime.now());
        User user2 = new User(1L, "test@example.com", "Doe", "John", "password123", true, LocalDateTime.now(),
                LocalDateTime.now());

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void shouldValidateToString() {
        User user = new User(1L, "test@example.com", "Doe", "John", "password123", true, LocalDateTime.now(),
                LocalDateTime.now());
        assertTrue(user.toString().contains("test@example.com"));
        assertTrue(user.toString().contains("Doe"));
        assertTrue(user.toString().contains("John"));
    }
}
