package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

    @Test
    void shouldHandleEmptyUsersList() {
        Session session = new Session();
        session.setUsers(new ArrayList<>());

        assertNotNull(session.getUsers());
        assertTrue(session.getUsers().isEmpty());
    }

    @Test
    void shouldAddUserToSession() {
        User user = new User(1L, "test@example.com", "Doe", "John", "password", false, null, null);
        Session session = new Session();
        session.setUsers(new ArrayList<>());

        session.getUsers().add(user);

        assertEquals(1, session.getUsers().size());
        assertTrue(session.getUsers().contains(user));
    }

    @Test
    void shouldHandleNullValuesGracefully() {
        Session session = new Session();

        assertNull(session.getName());
        assertNull(session.getDescription());
        assertNull(session.getTeacher());
    }

    @Test
    void shouldTestFullConstructor() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "test@example.com", "Doe", "John", "password123", true, now, now);

        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void shouldTestPartialConstructor() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "test@example.com", "Doe", "John", "password123", false, now, now);

        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertFalse(user.isAdmin());
    }

    @Test
    void shouldSetAndGetAttributes() {
        User user = new User();

        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");
        user.setAdmin(true);

        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isAdmin());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        User user1 = new User(1L, "test@example.com", "Doe", "John", "password123", true, now, now);
        User user2 = new User(1L, "test@example.com", "Doe", "John", "password123", true, now, now);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void shouldTestToString() {
        User user = new User(1L, "test@example.com", "Doe", "John", "password123", true, LocalDateTime.now(),
                LocalDateTime.now());
        String result = user.toString();

        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
        assertTrue(result.contains("password123"));
    }

    @Test
    void shouldBuildUserSuccessfully() {
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .build();

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
    }

    @Test
    void shouldTestToStringInUserBuilder() {
        User.UserBuilder userBuilder = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe");

        String result = userBuilder.toString();

        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("email=test@example.com"));
        assertTrue(result.contains("firstName=John"));
        assertTrue(result.contains("lastName=Doe"));
    }

    @Test
    void shouldEachAttributeInBuilder() {
        User.UserBuilder userBuilder = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123");

        assertEquals("test@example.com", userBuilder.build().getEmail());
        assertEquals("Doe", userBuilder.build().getLastName());
        assertEquals("John", userBuilder.build().getFirstName());
        assertEquals("password123", userBuilder.build().getPassword());
    }

    @Test
    void shouldTestFullConstructorWithValidValues() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "test@example.com", "Doe", "John", "password123", true, now, now);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void shouldTestEqualsWithSameObject() {
        User user = new User(1L, "test@example.com", "Doe", "John", "password123", true, null, null);
        assertEquals(user, user);
    }

    @Test
    void shouldTestEqualsWithDifferentId() {
        User user1 = new User(1L, "test@example.com", "Doe", "John", "password123", true, null, null);
        User user2 = new User(2L, "test@example.com", "Doe", "John", "password123", true, null, null);
        assertNotEquals(user1, user2);
    }

    @Test
    void shouldTestEqualsWithNull() {
        User user = new User(1L, "test@example.com", "Doe", "John", "password123", true, null, null);
        assertNotEquals(user, null);
    }

    @Test
    void shouldTestEqualsWithDifferentObjectType() {
        User user = new User(1L, "test@example.com", "Doe", "John", "password123", true, null, null);
        assertNotEquals(user, "A String Object");
    }

    @Test
    void shouldTestEqualsWithSameId() {
        User user1 = new User(1L, "test@example.com", "Doe", "John", "password123", true, null, null);
        User user2 = new User(1L, "other@example.com", "Jane", "Smith", "password123", false, null, null);
        assertEquals(user1, user2);
    }

    @Test
    void shouldTestSetters() {
        User user = new User();
        user.setId(2L);
        user.setEmail("newemail@example.com");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setPassword("newpassword");
        user.setAdmin(false);

        assertEquals(2L, user.getId());
        assertEquals("newemail@example.com", user.getEmail());
        assertEquals("Jane", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("newpassword", user.getPassword());
        assertFalse(user.isAdmin());
    }
}
