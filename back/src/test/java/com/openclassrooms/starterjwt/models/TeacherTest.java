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

    @Test
    void shouldHandleNullValuesGracefully() {
        Teacher teacher = new Teacher();

        assertNull(teacher.getLastName());
        assertNull(teacher.getFirstName());
        assertNull(teacher.getCreatedAt());
        assertNull(teacher.getUpdatedAt());
    }

    @Test
    void shouldValidateEqualityForNullFields() {
        Teacher teacher1 = new Teacher(1L, null, null, null, null);
        Teacher teacher2 = new Teacher(1L, null, null, null, null);

        assertEquals(teacher1, teacher2);
        assertEquals(teacher1.hashCode(), teacher2.hashCode());
    }

    @Test
    void shouldValidateToStringHandlesNulls() {
        Teacher teacher = new Teacher();
        String result = teacher.toString();
        assertTrue(result.contains("null"));
    }

    @Test
    void shouldBuildSessionSuccessfully() {
        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .description("A relaxing yoga session")
                .build();

        assertNotNull(session);
        assertEquals(1L, session.getId());
        assertEquals("Yoga Session", session.getName());
        assertEquals("A relaxing yoga session", session.getDescription());
    }

    @Test
    void shouldTestToStringInBuilder() {
        Session.SessionBuilder sessionBuilder = Session.builder()
                .id(1L)
                .name("Yoga Session");
        String result = sessionBuilder.toString();

        assertTrue(result.contains("Yoga Session"));
        assertTrue(result.contains("id=1"));
    }

    @Test
    void shouldBuildTeacherSuccessfully() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        assertNotNull(teacher);
        assertEquals(1L, teacher.getId());
        assertEquals("John", teacher.getFirstName());
        assertEquals("Doe", teacher.getLastName());
    }

    @Test
    void shouldTestToStringInTeacherBuilder() {
        Teacher.TeacherBuilder teacherBuilder = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe");

        String result = teacherBuilder.toString();

        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("firstName=John"));
        assertTrue(result.contains("lastName=Doe"));
    }
}
