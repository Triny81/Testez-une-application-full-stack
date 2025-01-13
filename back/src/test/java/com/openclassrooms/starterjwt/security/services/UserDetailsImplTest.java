package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    @Test
    void shouldBuildUserDetails() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(true)
                .build();

        assertEquals(1L, userDetails.getId());
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void shouldValidateEquality() {
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(1L)
                .username("other@example.com")
                .build();

        UserDetailsImpl user3 = UserDetailsImpl.builder()
                .id(2L)
                .username("test@example.com")
                .build();

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }

    @Test
    void shouldReturnEmptyAuthorities() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        assertNotNull(authorities);
        assertTrue(authorities instanceof HashSet);
        assertTrue(authorities.isEmpty());
    }
}
