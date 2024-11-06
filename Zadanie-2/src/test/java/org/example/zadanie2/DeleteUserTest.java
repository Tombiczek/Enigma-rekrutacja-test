package org.example.zadanie2;

import org.example.zadanie2.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteUserTest extends IntegrationTestBase {

    @Test
    public void deleteUserTest_success() throws Exception {

        UserDto user = createUser("Kamala", "Harris", "kamala.harris@example.com");

        ResultActions deleteResponse = mockMvc.perform(delete("/api/users/{userId}", user.getId())
                .contentType(MediaType.APPLICATION_JSON));

        deleteResponse.andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
