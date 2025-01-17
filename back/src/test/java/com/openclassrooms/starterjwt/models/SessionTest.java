package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    @Test
    void shouldSetAndGetValues() {
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Session session = new Session()
                .setId(1L)
                .setName("Yoga Session")
                .setDate(new Date())
                .setDescription("Relaxing yoga session")
                .setTeacher(teacher)
                .setUsers(Collections.emptyList())
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        assertEquals(1L, session.getId());
        assertEquals("Yoga Session", session.getName());
        assertNotNull(session.getDate());
        assertEquals("Relaxing yoga session", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertNotNull(session.getUsers());
        assertNotNull(session.getCreatedAt());
        assertNotNull(session.getUpdatedAt());
    }

    @Test
    void shouldValidateEqualsAndHashCode() {
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Session session1 = new Session(1L, "Yoga Session", new Date(), "Relaxing yoga session", teacher,
                Collections.emptyList(), LocalDateTime.now(), LocalDateTime.now());
        Session session2 = new Session(1L, "Yoga Session", new Date(), "Relaxing yoga session", teacher,
                Collections.emptyList(), LocalDateTime.now(), LocalDateTime.now());

        assertEquals(session1, session2);
        assertEquals(session1.hashCode(), session2.hashCode());
    }

    @Test
    void shouldValidateToString() {
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Session session = new Session(1L, "Yoga Session", new Date(), "Relaxing yoga session", teacher,
                Collections.emptyList(), LocalDateTime.now(), LocalDateTime.now());
        assertTrue(session.toString().contains("Yoga Session"));
        assertTrue(session.toString().contains("Relaxing yoga session"));
    }

    @Test
    void shouldHandleNullValuesGracefully() {
        User user = new User();

        assertNull(user.getEmail());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getPassword());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    void shouldValidateEqualityForNullFields() {
        User user1 = new User(null, "", "", "", "", false, null, null);
        User user2 = new User(null, "", "", "", "", false, null, null);

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void shouldValidateToStringHandlesNulls() {
        User user = new User();
        String result = user.toString();
        assertTrue(result.contains("null"));
    }
}
