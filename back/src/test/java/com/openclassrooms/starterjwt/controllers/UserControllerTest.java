package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldFindUserById() throws Exception {
        User user = new User(1L, "email@test.com", "Doe", "John", "password123", true, LocalDateTime.now(),
                LocalDateTime.now());
        UserDto userDto = new UserDto(1L, "email@test.com", "Doe", "John", true, "password123", LocalDateTime.now(),
                LocalDateTime.now());

        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("email@test.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        when(userService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldReturnBadRequestWhenIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/user/invalid-id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "USER")
    void shouldDeleteUserWhenAuthorized() throws Exception {
        User user = new User(1L, "test@example.com", "John", "Doe", "password", false, null, null);
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(delete("/api/user/1")).andExpect(status().isOk());

        verify(userService, times(1)).delete(1L);
    }

    @Test
    @WithMockUser(username = "wrong@example.com", roles = "USER")
    void shouldReturnUnauthorizedWhenUserIsNotAuthorized() throws Exception {
        User user = new User(1L, "test@example.com", "John", "Doe", "password", false, null, null);
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(delete("/api/user/1")).andExpect(status().isUnauthorized());

        verify(userService, never()).delete(anyLong());
    }
}
