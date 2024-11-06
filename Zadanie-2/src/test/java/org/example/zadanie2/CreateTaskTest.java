package org.example.zadanie2;

import org.example.zadanie2.dto.CreateTaskDto;
import org.example.zadanie2.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateTaskTest extends IntegrationTestBase {

    @Test
    public void createTaskTest_success() throws Exception {
        UserDto user1 = createUser("Kamala", "Harris", "kamala.harris@example.com");
        UserDto user2 = createUser("Donald", "Trump", "donald.trump@example.com");

        CreateTaskDto createTaskDto = new CreateTaskDto(
                "Complete Project",
                "Complete the final project for the client",
                "IN_PROGRESS",
                LocalDate.now().plusDays(7),
                Set.of(user1.getId(), user2.getId())
        );

        ResultActions response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTaskDto)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Complete Project"))
                .andExpect(jsonPath("$.description").value("Complete the final project for the client"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.assignedUserIds").value(containsInAnyOrder(user1.getId().intValue(), user2.getId().intValue())));
    }
}
