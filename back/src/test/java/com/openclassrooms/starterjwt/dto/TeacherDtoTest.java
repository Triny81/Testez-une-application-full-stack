package com.openclassrooms.starterjwt.dto;

import lombok.var;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TeacherDtoTest {

    private final Validator validator;

    public TeacherDtoTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void shouldSetAndGetValues() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Doe");
        teacherDto.setFirstName("John");
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now());

        assertEquals(1L, teacherDto.getId());
        assertEquals("Doe", teacherDto.getLastName());
        assertEquals("John", teacherDto.getFirstName());
        assertNotNull(teacherDto.getCreatedAt());
        assertNotNull(teacherDto.getUpdatedAt());
    }

    @Test
    void shouldValidateAnnotations() {
        TeacherDto teacherDto = new TeacherDto(null, "", "", null, null);

        var violations = validator.validate(teacherDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldTestConstructorsAndSetters() {
        // Test constructeur avec arguments
        TeacherDto teacherDto = new TeacherDto(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        assertEquals(1L, teacherDto.getId());
        assertEquals("Doe", teacherDto.getLastName());
        assertEquals("John", teacherDto.getFirstName());

        // Test setters
        teacherDto.setId(2L);
        teacherDto.setLastName("Smith");
        teacherDto.setFirstName("Jane");

        assertEquals(2L, teacherDto.getId());
        assertEquals("Smith", teacherDto.getLastName());
        assertEquals("Jane", teacherDto.getFirstName());
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        
        TeacherDto teacher1 = new TeacherDto(1L, "Doe", "John", now, now);
        TeacherDto teacher2 = new TeacherDto(1L, "Doe", "John", now, now);

        assertEquals(teacher1, teacher2);
        assertEquals(teacher1.hashCode(), teacher2.hashCode());

        teacher2.setId(2L);
        assertNotEquals(teacher1, teacher2);
    }

    @Test
    void shouldTestToString() {
        TeacherDto teacherDto = new TeacherDto(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        String toString = teacherDto.toString();
        assertTrue(toString.contains("Doe"));
        assertTrue(toString.contains("John"));
    }
}
