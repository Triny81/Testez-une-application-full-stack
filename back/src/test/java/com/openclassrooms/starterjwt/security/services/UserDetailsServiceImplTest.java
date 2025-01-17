package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    public UserDetailsServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldLoadUserByUsername() {
        User user = new User(1L, "test@example.com", "Doe", "John", "password123", true, null, null);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
        assertEquals("password123", userDetails.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("notfound@example.com"));
        assertEquals("User Not Found with email: notfound@example.com", exception.getMessage());
    }

    @Test
    void shouldTestToStringInUserDetailsImplBuilder() {
        UserDetailsImpl.UserDetailsImplBuilder builder = UserDetailsImpl.builder()
                .id(1L)
                .username("testUser");

        String result = builder.toString();

        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("username=testUser"));
    }
}
