package org.example.zadanie2;

import org.example.zadanie2.dto.TaskDto;
import org.example.zadanie2.dto.UpdateTaskDto;
import org.example.zadanie2.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EditTaskTest extends IntegrationTestBase {

    @Test
    public void editTaskTest_setStatusToCompleted_success() throws Exception {
        UserDto user = createUser("Kamala", "Harris", "kamala.harris@example.com");
        TaskDto task = createTask("Task 1", "Initial Description", "IN_PROGRESS", LocalDate.now().plusDays(1), Set.of(user.getId()));

        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        updateTaskDto.setStatus("COMPLETED");

        ResultActions response = mockMvc.perform(patch("/api/tasks/{taskId}", task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTaskDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    public void editTaskTest_assignAnotherUser_success() throws Exception {
        UserDto user1 = createUser("Kamala", "Harris", "kamala.harris@example.com");
        UserDto user2 = createUser("Donald", "Trump", "donald.trump@example.com");
        TaskDto task = createTask("Task 1", "Initial Description", "IN_PROGRESS", LocalDate.now().plusDays(1), Set.of(user1.getId()));

        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        updateTaskDto.setAssignedUserIds(Set.of(user1.getId(), user2.getId()));

        ResultActions response = mockMvc.perform(patch("/api/tasks/{taskId}", task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTaskDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.assignedUserIds", containsInAnyOrder(user1.getId().intValue(), user2.getId().intValue())));
    }

    @Test
    public void editTaskTest_updateTaskTitle_success() throws Exception {
        UserDto user = createUser("Kamala", "Harris", "kamala.harris@example.com");
        TaskDto task = createTask("Task 1", "Initial Description", "IN_PROGRESS", LocalDate.now().plusDays(1), Set.of(user.getId()));

        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        updateTaskDto.setTitle("Updated Task Title");

        ResultActions response = mockMvc.perform(patch("/api/tasks/{taskId}", task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTaskDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task Title"));
    }

    @Test
    public void editTaskTest_invalidStatus_badRequest() throws Exception {
        UserDto user = createUser("Kamala", "Harris", "kamala.harris@example.com");
        TaskDto task = createTask("Task 1", "Initial Description", "IN_PROGRESS", LocalDate.now().plusDays(1), Set.of(user.getId()));

        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        updateTaskDto.setStatus("INVALID_STATUS");

        ResultActions response = mockMvc.perform(patch("/api/tasks/{taskId}", task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTaskDto)));

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid status value"));
    }
}
