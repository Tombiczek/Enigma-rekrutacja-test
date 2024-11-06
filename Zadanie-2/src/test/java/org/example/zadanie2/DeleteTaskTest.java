package org.example.zadanie2;

import org.example.zadanie2.dto.TaskDto;
import org.example.zadanie2.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteTaskTest extends IntegrationTestBase {

    @Test
    public void deleteTaskTest_success() throws Exception {
        UserDto user = createUser("Kamala", "Harris", "kamala.harris@example.com");
        TaskDto task = createTask("Sample Task", "Task Description", "IN_PROGRESS", LocalDate.now().plusDays(5), Set.of(user.getId()));

        ResultActions deleteResponse = mockMvc.perform(delete("/api/tasks/{taskId}", task.getId())
                .contentType(MediaType.APPLICATION_JSON));

        deleteResponse.andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/{taskId}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteTask_nonExistentTask_notFound() throws Exception {
        Long taskId = 999L;

        ResultActions response = mockMvc.perform(delete("/api/tasks/{taskId}", taskId)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task with ID " + taskId + " not found"));
    }
}
