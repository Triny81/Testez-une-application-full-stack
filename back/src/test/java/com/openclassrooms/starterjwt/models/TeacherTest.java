package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    void shouldSetAndGetValues() {
        Teacher teacher = new Teacher()
                .setId(1L)
                .setLastName("Doe")
                .setFirstName("John")
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        assertEquals(1L, teacher.getId());
        assertEquals("Doe", teacher.getLastName());
        assertEquals("John", teacher.getFirstName());
        assertNotNull(teacher.getCreatedAt());
        assertNotNull(teacher.getUpdatedAt());
    }

    @Test
    void shouldValidateEqualsAndHashCode() {
        Teacher teacher1 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher2 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        assertEquals(teacher1, teacher2);
        assertEquals(teacher1.hashCode(), teacher2.hashCode());
    }

    @Test
    void shouldValidateToString() {
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        assertTrue(teacher.toString().contains("Doe"));
        assertTrue(teacher.toString().contains("John"));
    }
}
