package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {

    @Test
    void shouldTestConstructorAndGetter() {
        String message = "Operation successful";
        MessageResponse messageResponse = new MessageResponse(message);
        assertEquals("Operation successful", messageResponse.getMessage());
    }

    @Test
    void shouldTestSetter() {
        MessageResponse messageResponse = new MessageResponse(null);
        messageResponse.setMessage("New message");
        assertEquals("New message", messageResponse.getMessage());
    }
}
