package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long testUserId;

    @BeforeEach
    void setUp() {
        User user = new User(null, "test@example.com", "Doe", "John", passwordEncoder.encode("password123"), false,
                LocalDateTime.now(),
                LocalDateTime.now());

        User savedUser = userRepository.save(user);
        testUserId = savedUser.getId();
    }

    @AfterEach
    void clearEverything() {
        userRepository.deleteById(testUserId);
    }

    @Test
    void shouldAuthenticateUser() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("{\"email\": \"test@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.admin").value(false));
    }

    @Test
    void shouldFailAuthenticationWithInvalidCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(
                        "{\"email\":\"john@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"password\":\"password123\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRegisterNewUser() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(
                        "{\"email\":\"janny@test.com\",\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        User registeredUser = userRepository.findByEmail("janny@test.com").orElse(null);
        assert registeredUser != null;
        assert registeredUser.getFirstName().equals("Jane");
        assert registeredUser.getLastName().equals("Smith");

        userRepository.delete(registeredUser);
    }

    @Test
    void shouldNotRegisterDuplicateEmail() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content("{\"email\":\"test@example.com\",\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"password\":\"password123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
    }
}
