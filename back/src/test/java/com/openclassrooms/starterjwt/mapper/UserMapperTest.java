package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void shouldMapToEntity() {
        UserDto userDto = new UserDto(1L, "test@example.com", "Doe", "John", true, "password123", LocalDateTime.now(),
                LocalDateTime.now());

        User user = userMapper.toEntity(userDto);

        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.isAdmin(), user.isAdmin());
        assertEquals(userDto.getPassword(), user.getPassword());
    }

    @Test
    void shouldMapToDto() {
        User user = new User(1L, "test@example.com", "Doe", "John", "password123", true, LocalDateTime.now(),
                LocalDateTime.now());

        UserDto userDto = userMapper.toDto(user);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.isAdmin(), userDto.isAdmin());
    }

    @Test
    void shouldMapEntityWithNullFieldsToDto() {
        User user = new User(null, "", "", "", "", false, null, null);
        UserDto userDto = userMapper.toDto(user);

        assertNotNull(userDto);
        assertNull(userDto.getId());
        assertFalse(userDto.isAdmin());

        assertNotNull(userDto.getEmail());
        assertTrue(userDto.getEmail().isEmpty());

        assertNotNull(userDto.getLastName());
        assertTrue(userDto.getLastName().isEmpty());

        assertNotNull(userDto.getFirstName());
        assertTrue(userDto.getFirstName().isEmpty());

        assertNotNull(userDto.getPassword());
        assertTrue(userDto.getPassword().isEmpty());
    }

    @Test
    void shouldMapDtoWithNullFieldsToEntity() {
        UserDto userDto = new UserDto(null, "", "", "", false, "", null, null);
        User user = userMapper.toEntity(userDto);

        assertNotNull(user);
        assertNull(user.getId());
        assertFalse(user.isAdmin());

        assertNotNull(user.getEmail());
        assertTrue(user.getEmail().isEmpty());

        assertNotNull(user.getLastName());
        assertTrue(user.getLastName().isEmpty());

        assertNotNull(user.getFirstName());
        assertTrue(user.getFirstName().isEmpty());

        assertNotNull(user.getPassword());
        assertTrue(user.getPassword().isEmpty());
    }

    @Test
    void shouldMapDtoListToEntityList() {
        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(new UserDto(1L, "test1@example.com", "John", "Doe", true, "password1", LocalDateTime.now(),
                LocalDateTime.now()));
        userDtoList.add(new UserDto(2L, "test2@example.com", "Jane", "Smith", false, "password2", LocalDateTime.now(),
                LocalDateTime.now()));

        List<User> userList = userMapper.toEntity(userDtoList);

        assertNotNull(userList);
        assertEquals(2, userList.size());
        assertEquals("test1@example.com", userList.get(0).getEmail());
        assertEquals("test2@example.com", userList.get(1).getEmail());
    }

    @Test
    void shouldMapEntityListToDtoList() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "test1@example.com", "John", "Doe", "password1", true, LocalDateTime.now(),
                LocalDateTime.now()));
        userList.add(new User(2L, "test2@example.com", "Jane", "Smith", "password2", false, LocalDateTime.now(),
                LocalDateTime.now()));

        List<UserDto> userDtoList = userMapper.toDto(userList);

        assertNotNull(userDtoList);
        assertEquals(2, userDtoList.size());
        assertEquals("test1@example.com", userDtoList.get(0).getEmail());
        assertEquals("test2@example.com", userDtoList.get(1).getEmail());
    }
}
