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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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

                SessionDto sessionDto = new SessionDto(1L, "Yoga Session", new Date(), 1L, "Relaxing yoga session",
                                Arrays.asList(1L, 2L), LocalDateTime.now(), LocalDateTime.now());

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
                Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
                ;
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
                                new SessionDto(1L, "Yoga Session", new Date(), 1L, "Relaxing yoga session",
                                                Arrays.asList(1L), LocalDateTime.now(), LocalDateTime.now()),
                                new SessionDto(2L, "Meditation Session", new Date(), 1L, "Calming meditation session",
                                                Arrays.asList(2L), LocalDateTime.now(), LocalDateTime.now()));

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
        void shouldCreateYogaSession() throws Exception {
                Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

                Session session = Session.builder().id(5L).name("Ma session").date(new Date())
                                .description("Description").teacher(teacher).users(Collections.emptyList())
                                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

                SessionDto sessionDtoResponse = new SessionDto(5L, "Ma session", new Date(), 1L, "Description",
                                Collections.emptyList(), LocalDateTime.now(), LocalDateTime.now());

                when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
                when(sessionService.create(any(Session.class))).thenReturn(session);
                when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDtoResponse);

                mockMvc.perform(post("/api/session")
                                .contentType("application/json")
                                .content("{\"name\":\"Ma session\",\"date\":\"2025-01-01\",\"teacher_id\":1,\"description\":\"Description\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Ma session"))
                                .andExpect(jsonPath("$.description").value("Description"))
                                .andExpect(jsonPath("$.teacher_id").value(1))
                                .andExpect(jsonPath("$.users.length()").value(0));
        }

        @Test
        @WithMockUser(username = "testUser", roles = "USER")
        void shouldCreateSession() throws Exception {
                SessionDto sessionDto = new SessionDto(1L, "Yoga Session", new Date(), 1L, "Relaxing yoga",
                                Collections.emptyList(), null, null);
                Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
                Session session = new Session(1L, "Yoga Session", new Date(), "Relaxing yoga", teacher,
                                Collections.emptyList(), LocalDateTime.now(), LocalDateTime.now());

                when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
                when(sessionService.create(any(Session.class))).thenReturn(session);
                when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

                mockMvc.perform(post("/api/session")
                                .contentType("application/json")
                                .content("{\"name\": \"Yoga Session\", \"date\": \"2025-01-01\", \"teacher_id\": 1, \"description\": \"Relaxing yoga\"}"))
                                .andExpect(status().isOk());

                verify(sessionService, times(1)).create(any(Session.class));
        }

        @Test
        @WithMockUser(username = "testUser", roles = "USER")
        void shouldParticipateInSession() throws Exception {
                mockMvc.perform(post("/api/session/1/participate/2")
                                .contentType("application/json"))
                                .andExpect(status().isOk());

                verify(sessionService, times(1)).participate(1L, 2L);
        }

        @Test
        @WithMockUser(username = "testUser", roles = "USER")
        void shouldNoLongerParticipateInSession() throws Exception {
                mockMvc.perform(delete("/api/session/1/participate/2")
                                .contentType("application/json"))
                                .andExpect(status().isOk());

                verify(sessionService, times(1)).noLongerParticipate(1L, 2L);
        }
}
