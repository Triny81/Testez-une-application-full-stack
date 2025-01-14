package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    private Long testTeacherId;
    private Long testTeacherId2;

    @BeforeEach
    void setUp() {
        Teacher teacher = new Teacher()
                .setFirstName("Jane")
                .setLastName("Smith");
        Teacher savedTeacher = teacherRepository.save(teacher);
        testTeacherId = savedTeacher.getId();

        Teacher teacher2 = new Teacher()
                .setFirstName("John")
                .setLastName("Doe");
        Teacher savedTeacher2 = teacherRepository.save(teacher2);
        testTeacherId2 = savedTeacher2.getId();
    }

    @AfterEach
    void clearEverything() {
        teacherRepository.deleteById(testTeacherId);
        teacherRepository.deleteById(testTeacherId2);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldFindTeacherById() throws Exception {
        mockMvc.perform(get("/api/teacher/" + testTeacherId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void shouldFindAllTeachers() throws Exception {
        long initialCount = teacherRepository.count();

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(initialCount)) // calculer combien il y a de teachers en BD
                .andExpect(jsonPath("$[-2].firstName").value("Jane"))
                .andExpect(jsonPath("$[-1].firstName").value("John"));
    }
}
