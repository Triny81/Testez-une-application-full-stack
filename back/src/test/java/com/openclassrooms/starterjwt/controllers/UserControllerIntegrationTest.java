package com.openclassrooms.starterjwt.controllers;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private Long testUserId;
    private Long testAdminId;

    @BeforeEach
    void setUp() {
        User user = new User(null, "john@test.com", "Doe", "John", "password123", false, LocalDateTime.now(),
                LocalDateTime.now());

        User savedUser = userRepository.save(user);
        testUserId = savedUser.getId();

        User admin = new User(null, "jane@test.com", "Smith", "Jane", "password123", true, LocalDateTime.now(),
                LocalDateTime.now());

        User testAdmin = userRepository.save(admin);
        testAdminId = testAdmin.getId();
    }

    @AfterEach
    void clearEverything() {
        if (userRepository.existsById(testUserId)) {
            userRepository.deleteById(testUserId);
        }
        
        userRepository.deleteById(testAdminId);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldFindUserById() throws Exception {
        mockMvc.perform(get("/api/user/"+testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@test.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @WithMockUser(username = "john@test.com", roles = "USER")
    void shouldDeleteAuthorizedUser() throws Exception {       
        mockMvc.perform(delete("/api/user/" + testUserId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/user/" + testUserId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "john@test.com", roles = "USER")
    void shouldNotAllowDeletionOfOtherUser() throws Exception {
        mockMvc.perform(delete("/api/user/" + testAdminId))
                .andExpect(status().isUnauthorized());
    }
}
