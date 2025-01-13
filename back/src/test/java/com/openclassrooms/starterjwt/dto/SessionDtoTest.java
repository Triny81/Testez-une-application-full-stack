package com.openclassrooms.starterjwt.dto;

import lombok.var;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SessionDtoTest {

    private final Validator validator;

    public SessionDtoTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void shouldSetAndGetValues() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Relaxing yoga session");
        sessionDto.setUsers(List.of(1L, 2L));
        sessionDto.setCreatedAt(LocalDateTime.now());
        sessionDto.setUpdatedAt(LocalDateTime.now());

        assertEquals(1L, sessionDto.getId());
        assertEquals("Yoga Session", sessionDto.getName());
        assertNotNull(sessionDto.getDate());
        assertEquals(1L, sessionDto.getTeacher_id());
        assertEquals("Relaxing yoga session", sessionDto.getDescription());
        assertEquals(2, sessionDto.getUsers().size());
        assertNotNull(sessionDto.getCreatedAt());
        assertNotNull(sessionDto.getUpdatedAt());
    }

    @Test
    void shouldValidateAnnotations() {
        SessionDto sessionDto = new SessionDto(null, "", null, null, "", null, null, null);

        var violations = validator.validate(sessionDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldTestConstructorsAndSetters() {
        SessionDto sessionDto = new SessionDto(
                1L, "Yoga Session", new Date(), 1L, "Description", List.of(1L, 2L), LocalDateTime.now(),
                LocalDateTime.now());

        assertEquals(1L, sessionDto.getId());
        assertEquals("Yoga Session", sessionDto.getName());
        assertNotNull(sessionDto.getDate());
        assertEquals(1L, sessionDto.getTeacher_id());
        assertEquals("Description", sessionDto.getDescription());
        assertEquals(2, sessionDto.getUsers().size());

        // Test setters
        sessionDto.setName("New Name");
        sessionDto.setTeacher_id(2L);
        sessionDto.setUsers(List.of());

        assertEquals("New Name", sessionDto.getName());
        assertEquals(2L, sessionDto.getTeacher_id());
        assertTrue(sessionDto.getUsers().isEmpty());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();

        SessionDto session1 = new SessionDto(1L, "Yoga", new Date(), 1L, "Desc", List.of(1L), now, now);
        SessionDto session2 = new SessionDto(1L, "Yoga", new Date(), 1L, "Desc", List.of(1L), now, now);

        assertEquals(session1, session2);
        assertEquals(session1.hashCode(), session2.hashCode());

        session2.setId(2L);
        assertNotEquals(session1, session2);
    }

    @Test
    void shouldTestToString() {
        SessionDto sessionDto = new SessionDto(1L, "Yoga", new Date(), 1L, "Desc", List.of(1L), LocalDateTime.now(),
                LocalDateTime.now());

        String toString = sessionDto.toString();
        assertTrue(toString.contains("Yoga"));
        assertTrue(toString.contains("Desc"));
    }
}
