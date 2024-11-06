package org.example.zadanie2;

import org.example.zadanie2.dto.CreateTaskDto;
import org.example.zadanie2.dto.CreateUserDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateUserTest extends IntegrationTestBase {

    @Test
    public void createUserTest_success() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto("Kamala", "Harris", "kamala.harris@example.com");

        ResultActions response = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDto)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Kamala"))
                .andExpect(jsonPath("$.lastName").value("Harris"))
                .andExpect(jsonPath("$.email").value("kamala.harris@example.com"));
    }

    @Test
    public void createUserTest_invalidEmail_badRequest() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto("Kamala", "Harris", "some-random-text");

        ResultActions response = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDto)));

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email should be valid"));
    }

    @Test
    public void createUserTest_noTitleProvided_badRequest() throws Exception {
        CreateTaskDto createTaskDto = new CreateTaskDto(
                null,
                "Complete the final project for the client",
                "IN_PROGRESS",
                LocalDate.now().plusDays(7),
                Set.of(1L)
        );

        ResultActions response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTaskDto)));

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Title is required"));
    }

    @Test
    public void createUserTest_dueDateIsPast_badRequest() throws Exception {
        CreateTaskDto createTaskDto = new CreateTaskDto(
                "Complete Project",
                "Complete the final project for the client",
                "IN_PROGRESS",
                LocalDate.now().minusDays(1),
                Set.of(1L)
        );

        ResultActions response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTaskDto)));

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Due date must be in the present or future"));
    }
}
