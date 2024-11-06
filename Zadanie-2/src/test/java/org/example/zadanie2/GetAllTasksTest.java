package org.example.zadanie2;

import org.example.zadanie2.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetAllTasksTest extends IntegrationTestBase {

    @Test
    public void getAllTasksTest_success() throws Exception {
        UserDto user = createUser("Kamala", "Harris", "kamala.harris@example.com");
        createTask("Task 1", "Description for task 1", "IN_PROGRESS", LocalDate.now().plusDays(1), Set.of(user.getId()));
        createTask("Task 2", "Description for task 2", "COMPLETED", LocalDate.now().plusDays(5), Set.of(user.getId()));

        ResultActions response = mockMvc.perform(get("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    public void getAllTasksTest_filterByStatus_success() throws Exception {
        UserDto user = createUser("Donald", "Trump", "donald.trump@example.com");
        createTask("Task A", "Description for task A", "IN_PROGRESS", LocalDate.now().plusDays(2), Set.of(user.getId()));
        createTask("Task B", "Description for task B", "COMPLETED", LocalDate.now().plusDays(3), Set.of(user.getId()));

        ResultActions response = mockMvc.perform(get("/api/tasks")
                .param("status", "IN_PROGRESS")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Task A"))
                .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"));
    }
}
