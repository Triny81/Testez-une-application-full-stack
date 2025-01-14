package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    private Long testSessionId;
    private Long testSessionId2;
    private Long testTeacherId;
    private Long testUserId;

    private Date now = new Date();

    @BeforeEach
    void setUp() {
        Teacher teacher = new Teacher()
                .setFirstName("John")
                .setLastName("Doe");
        Teacher savedTeacher = teacherRepository.save(teacher);
        testTeacherId = savedTeacher.getId();

        Session session = new Session()
                .setName("Yoga Session")
                .setDescription("Relaxing yoga session")
                .setDate(now)
                .setTeacher(savedTeacher);
        Session savedSession = sessionRepository.save(session);
        testSessionId = savedSession.getId();

        Session session2 = new Session()
                .setName("Yoga Session beginners")
                .setDescription("Relaxing yoga session for beginners")
                .setDate(now)
                .setTeacher(teacher);
        Session savedSession2 = sessionRepository.save(session2);
        testSessionId2 = savedSession2.getId();

        User user = new User(null, "email@test.com", "Doe", "John", "password123", true, LocalDateTime.now(),
                LocalDateTime.now());

        User savedUser = userRepository.save(user);
        testUserId = savedUser.getId();
    }

    @AfterEach
    void clearEverything() {
        sessionRepository.deleteById(testSessionId);
        sessionRepository.deleteById(testSessionId2);
        teacherRepository.deleteById(testTeacherId);
        userRepository.deleteById(testUserId);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldFindSessionById() throws Exception {

        mockMvc.perform(get("/api/session/" + testSessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga Session"))
                .andExpect(jsonPath("$.description").value("Relaxing yoga session"))
                .andExpect(jsonPath("$.teacher_id").value(testTeacherId));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldFindAllSessions() throws Exception {
        long initialCount = sessionRepository.count();

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(initialCount))
                .andExpect(jsonPath("$[-2].name").value("Yoga Session"))
                .andExpect(jsonPath("$[-1].name").value("Yoga Session beginners"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldCreateAndDeleteSession() throws Exception {
        String newSessionJson = "{\"name\":\"Ma session\",\"date\":\"2025-01-01\",\"teacher_id\":" + testTeacherId
                + ",\"description\":\"Description\"}";

        String response = mockMvc.perform(post("/api/session")
                .contentType("application/json")
                .content(newSessionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ma session"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        Long createdSessionId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/session/" + createdSessionId))
                .andExpect(status().isOk()); // Supprimer la session fraîchement créée
        
        mockMvc.perform(get("/api/session/" + createdSessionId))
                .andExpect(status().isNotFound()); // Vérifier que la session a bien été supprimée
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldUpdateSession() throws Exception {
        String updatedSessionJson = "{\"name\":\"Ma session modifiée\",\"date\":\"2025-01-02\",\"teacher_id\":"
                + testTeacherId + ",\"description\":\"Description modifiée\"}";

        mockMvc.perform(put("/api/session/" + testSessionId)
                .contentType("application/json")
                .content(updatedSessionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ma session modifiée"))
                .andExpect(jsonPath("$.description").value("Description modifiée"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldAddAndRemoveParticipantToSession() throws Exception {
        mockMvc.perform(post("/api/session/" + testSessionId + "/participate/" + testUserId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/session/" + testSessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").value(org.hamcrest.Matchers.hasItem(testUserId.intValue()))); // Vérifier que l'utilisateur participe

        mockMvc.perform(delete("/api/session/" + testSessionId + "/participate/" + testUserId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/session/" + testSessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").value(org.hamcrest.Matchers.not(org.hamcrest.Matchers.hasItem(testUserId.intValue())))); // Vérifier que l'utilisateur ne participe plus
    }
}
