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

    @Test
    void shouldReturnAdminStatus() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testUser", "John", "Doe", true, "password");
        boolean isAdmin = userDetails.getAdmin();
        assertTrue(isAdmin);
    }

    @Test
    void shouldReturnNonAdminStatus() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testUser", "John", "Doe", false, "password");
        boolean isAdmin = userDetails.getAdmin();
        assertFalse(isAdmin);
    }

    @Test
    void shouldTestEqualsForDifferentObjects() {
        UserDetailsImpl user1 = new UserDetailsImpl(1L, "testUser", "John", "Doe", true, "password");
        UserDetailsImpl user2 = new UserDetailsImpl(2L, "differentUser", "Jane", "Smith", false, "password");

        assertNotEquals(user1, user2);
    }

    @Test
    void shouldTestEqualsForNullObject() {
        UserDetailsImpl user = new UserDetailsImpl(1L, "testUser", "John", "Doe", true, "password");
        assertNotEquals(user, null);
    }

    @Test
    void shouldTestEqualsForDifferentTypeObject() {
        UserDetailsImpl user = new UserDetailsImpl(1L, "testUser", "John", "Doe", true, "password");
        assertNotEquals(user, "a string");
    }
}
