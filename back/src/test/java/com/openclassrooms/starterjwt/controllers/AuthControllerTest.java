package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        when(userRepository.existsByEmail("johndoe@example.com")).thenReturn(false);

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(
                        "{\"email\":\"johndoe@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() throws Exception {
        when(userRepository.existsByEmail("johndoe@example.com")).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(
                        "{\"email\":\"johndoe@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"password\":\"password123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
    }
}
