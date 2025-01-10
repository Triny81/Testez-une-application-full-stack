package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldFindSessionById() throws Exception {
        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(new Date())
                .description("Relaxing yoga session")
                .teacher(new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        SessionDto sessionDto = new SessionDto(1L, "Yoga Session", new Date(), 1L, "Relaxing yoga session", Arrays.asList(1L, 2L), LocalDateTime.now(), LocalDateTime.now());

        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);


        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga Session"))
                .andExpect(jsonPath("$.description").value("Relaxing yoga session"))
                .andExpect(jsonPath("$.teacher_id").value(1))
                .andExpect(jsonPath("$.users.length()").value(2));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldReturnNotFoundWhenSessionDoesNotExist() throws Exception {
        when(sessionService.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldFindAllSessions() throws Exception {
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());;
        Session session1 = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(new Date())
                .description("Relaxing yoga session")
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Session session2 = Session.builder()
                .id(2L)
                .name("Meditation Session")
                .date(new Date())
                .description("Calming meditation session")
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Session> sessions = Arrays.asList(session1, session2);
        List<SessionDto> sessionDtos = Arrays.asList(
                new SessionDto(1L, "Yoga Session", new Date(), 1L, "Relaxing yoga session", Arrays.asList(1L), LocalDateTime.now(), LocalDateTime.now()),
                new SessionDto(2L, "Meditation Session", new Date(), 1L, "Calming meditation session", Arrays.asList(2L), LocalDateTime.now(), LocalDateTime.now())
        );

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Yoga Session"))
                .andExpect(jsonPath("$[1].name").value("Meditation Session"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldCreateSession() throws Exception {
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());;
        SessionDto sessionDto = new SessionDto(null, "Yoga Session", new Date(), 1L, "Relaxing yoga session", Arrays.asList(1L), LocalDateTime.now(), LocalDateTime.now());
        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(new Date())
                .description("Relaxing yoga session")
                .teacher(teacher)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(new SessionDto(1L, "Yoga Session", new Date(), 1L, "Relaxing yoga session", Arrays.asList(1L), LocalDateTime.now(), LocalDateTime.now()));

        mockMvc.perform(post("/api/session")
                        .contentType("application/json")
                        .content("{\"name\":\"Yoga Session\",\"description\":\"Relaxing yoga session\",\"teacher_id\":1,\"date\":\"2025-01-01T12:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga Session"))
                .andExpect(jsonPath("$.description").value("Relaxing yoga session"));
    }
}
