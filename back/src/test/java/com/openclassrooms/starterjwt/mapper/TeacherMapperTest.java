package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    void shouldMapToEntity() {
        TeacherDto teacherDto = new TeacherDto(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
    }

    @Test
    void shouldMapToDto() {
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacher.getLastName(), teacherDto.getLastName());
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
    }

    @Test
    void shouldMapEntityWithNullFieldsToDto() {
        Teacher teacher = new Teacher(null, null, null, null, null);
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertNotNull(teacherDto);
        assertNull(teacherDto.getId());
        assertNull(teacherDto.getLastName());
        assertNull(teacherDto.getFirstName());
    }

    @Test
    void shouldMapDtoWithNullFieldsToEntity() {
        TeacherDto teacherDto = new TeacherDto(null, null, null, null, null);
        Teacher teacher = teacherMapper.toEntity(teacherDto);
        
        assertNotNull(teacher);
        assertNull(teacher.getId());
        assertNull(teacher.getLastName());
        assertNull(teacher.getFirstName());
    }

    @Test
    void shouldMapDtoListToEntityList() {

        List<TeacherDto> teacherDtoList = new ArrayList<>();
        teacherDtoList.add(new TeacherDto(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now()));
        teacherDtoList.add(new TeacherDto(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now()));

        List<Teacher> teacherList = teacherMapper.toEntity(teacherDtoList);

        assertNotNull(teacherList);
        assertEquals(2, teacherList.size());
        assertEquals("Doe", teacherList.get(0).getLastName());
        assertEquals("Smith", teacherList.get(1).getLastName());
    }

    @Test
    void shouldMapEntityListToDtoList() {
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now()));
        teacherList.add(new Teacher(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now()));

        List<TeacherDto> teacherDtoList = teacherMapper.toDto(teacherList);
        
        assertNotNull(teacherDtoList);
        assertEquals(2, teacherDtoList.size());
        assertEquals("Doe", teacherDtoList.get(0).getLastName());
        assertEquals("Smith", teacherDtoList.get(1).getLastName());
    }
}
