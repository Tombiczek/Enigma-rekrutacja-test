package org.example.zadanie2;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetAllUsersTest extends IntegrationTestBase {

    @Test
    public void getAllUsersTest_success() throws Exception {
        createUser("Kamala", "Harris", "kamala.harris@example.com");
        createUser("Donald", "Trump", "donald.trump@example.com");

        ResultActions response = mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Kamala"))
                .andExpect(jsonPath("$[1].firstName").value("Donald"));
    }

    @Test
    public void getAllUsers_filterByEmail_success() throws Exception {
        createUser("Joe", "Biden", "joe.biden@example.com");
        createUser("Barack", "Obama", "barack.obama@example.com");

        ResultActions response = mockMvc.perform(get("/api/users")
                .param("email", "joe.biden@example.com")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Joe"))
                .andExpect(jsonPath("$[0].email").value("joe.biden@example.com"));
    }
}
