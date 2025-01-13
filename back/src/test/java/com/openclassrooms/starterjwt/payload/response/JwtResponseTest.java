package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {

    @Test
    void shouldTestConstructorAndGetters() {
        String token = "sample-token";
        Long id = 1L;
        String username = "testUser";
        String firstName = "John";
        String lastName = "Doe";
        Boolean admin = true;

        JwtResponse jwtResponse = new JwtResponse(token, id, username, firstName, lastName, admin);

        assertEquals("sample-token", jwtResponse.getToken());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("testUser", jwtResponse.getUsername());
        assertEquals("John", jwtResponse.getFirstName());
        assertEquals("Doe", jwtResponse.getLastName());
        assertTrue(jwtResponse.getAdmin());
        assertEquals("Bearer", jwtResponse.getType());
    }

    @Test
    void shouldTestSetters() {
        JwtResponse jwtResponse = new JwtResponse(null, null, null, null, null, null);

        jwtResponse.setToken("new-token");
        jwtResponse.setId(2L);
        jwtResponse.setUsername("newUser");
        jwtResponse.setFirstName("Jane");
        jwtResponse.setLastName("Smith");
        jwtResponse.setAdmin(false);

        assertEquals("new-token", jwtResponse.getToken());
        assertEquals(2L, jwtResponse.getId());
        assertEquals("newUser", jwtResponse.getUsername());
        assertEquals("Jane", jwtResponse.getFirstName());
        assertEquals("Smith", jwtResponse.getLastName());
        assertFalse(jwtResponse.getAdmin());
    }
}
