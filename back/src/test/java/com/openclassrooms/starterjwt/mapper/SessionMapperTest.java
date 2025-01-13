package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SessionMapperTest {

    @Autowired
    private SessionMapper sessionMapper;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private UserService userService;

    @Test
    void shouldMapToEntity() {
        SessionDto sessionDto = new SessionDto(
                null,
                "Yoga Session",
                new Date(),
                1L,
                "Relaxing yoga session",
                List.of(1L, 2L),
                null,
                null);

        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        User user1 = new User(1L, "user1@example.com", "User1", "LastName1", "password", false, LocalDateTime.now(),
                LocalDateTime.now());
        User user2 = new User(2L, "user2@example.com", "User2", "LastName2", "password", false, LocalDateTime.now(),
                LocalDateTime.now());

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        Session session = sessionMapper.toEntity(sessionDto);

        assertEquals("Yoga Session", session.getName());
        assertEquals(teacher, session.getTeacher());
        assertEquals(2, session.getUsers().size());
    }

    @Test
    void shouldMapToDto() {
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Session session = new Session(
                1L,
                "Yoga Session",
                new Date(),
                "Relaxing yoga session",
                teacher,
                Collections.emptyList(),
                LocalDateTime.now(),
                LocalDateTime.now());

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertEquals("Yoga Session", sessionDto.getName());
        assertEquals("Relaxing yoga session", sessionDto.getDescription());
        assertEquals(1L, sessionDto.getTeacher_id());
        assertNotNull(sessionDto.getDate());
    }

    @Test
    void shouldMapEntityWithNullFieldsToDto() {
        Session session = new Session(null, null, null, null, null, null, null, null);
        SessionDto sessionDto = sessionMapper.toDto(session);

        assertNotNull(sessionDto);
        assertNull(sessionDto.getId());
        assertNull(sessionDto.getName());
        assertNull(sessionDto.getDate());
        assertNull(sessionDto.getTeacher_id());
        assertNull(sessionDto.getDescription());

        assertNotNull(sessionDto.getUsers());
        assertTrue(sessionDto.getUsers().isEmpty());
    }

    @Test
    void shouldMapDtoWithNullFieldsToEntity() {
        SessionDto sessionDto = new SessionDto(null, null, null, null, null, null, null, null);
        Session session = sessionMapper.toEntity(sessionDto);

        assertNotNull(session);
        assertNull(session.getId());
        assertNull(session.getName());  
        assertNull(session.getDate());
        assertNull(session.getDescription());
        assertNull(session.getTeacher());

        assertNotNull(session.getUsers());
        assertTrue(session.getUsers().isEmpty());
    }

    @Test
    void shouldMapDtoListToEntityList() {
        List<SessionDto> sessionDtoList = new ArrayList<>();
        sessionDtoList.add(new SessionDto(1L, "Session 1", new Date(), 1L, "Description 1", List.of(1L), LocalDateTime.now(), LocalDateTime.now()));
        sessionDtoList.add(new SessionDto(2L, "Session 2", new Date(), 2L, "Description 2", List.of(2L), LocalDateTime.now(), LocalDateTime.now()));

        List<Session> sessionList = sessionMapper.toEntity(sessionDtoList);

        assertNotNull(sessionList);
        assertEquals(2, sessionList.size());
        assertEquals("Session 1", sessionList.get(0).getName());
        assertEquals("Session 2", sessionList.get(1).getName());
    }

    @Test
    void shouldMapEntityListToDtoList() {
        List<Session> sessionList = new ArrayList<>();
        sessionList.add(new Session(1L, "Session 1", new Date(), "Description 1", null, null, LocalDateTime.now(), LocalDateTime.now()));
        sessionList.add(new Session(2L, "Session 2", new Date(), "Description 2", null, null, LocalDateTime.now(), LocalDateTime.now()));

        List<SessionDto> sessionDtoList = sessionMapper.toDto(sessionList);

        assertNotNull(sessionDtoList);
        assertEquals(2, sessionDtoList.size());
        assertEquals("Session 1", sessionDtoList.get(0).getName());
        assertEquals("Session 2", sessionDtoList.get(1).getName());
    }
}
