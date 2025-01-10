package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    public SessionServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateSession() {
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(new Date())
                .description("A relaxing yoga session")
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.create(session);

        assertNotNull(result);
        assertEquals("Yoga Session", result.getName());
        assertEquals("A relaxing yoga session", result.getDescription());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void shouldFindAllSessions() {
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        Session session1 = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(new Date())
                .description("A relaxing yoga session")
                .teacher(teacher)
                .users(new ArrayList<>())
                .build();
        Session session2 = Session.builder()
                .id(2L)
                .name("Meditation Session")
                .date(new Date())
                .description("A calming meditation session")
                .teacher(teacher)
                .users(new ArrayList<>())
                .build();
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(session1, session2));

        List<Session> sessions = sessionService.findAll();

        assertNotNull(sessions);
        assertEquals(2, sessions.size());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void shouldParticipateInSession() {
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        User user = new User(1L, "email@test.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());
        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(new Date())
                .description("A relaxing yoga session")
                .teacher(teacher)
                .users(new ArrayList<>())
                .build();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        sessionService.participate(1L, 1L);

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSessionNotFoundForParticipation() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void shouldThrowBadRequestExceptionWhenUserAlreadyParticipates() {
        Teacher teacher = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
        User user = new User(1L, "email@test.com", "Doe", "John", "password123", true, LocalDateTime.now(), LocalDateTime.now());
        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(new Date())
                .description("A relaxing yoga session")
                .teacher(teacher)
                .users(new ArrayList<>(List.of(user)))
                .build();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
    }
}
