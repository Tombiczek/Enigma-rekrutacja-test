package org.example.zadanie2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.zadanie2.dto.CreateTaskDto;
import org.example.zadanie2.dto.CreateUserDto;
import org.example.zadanie2.dto.TaskDto;
import org.example.zadanie2.dto.UserDto;
import org.example.zadanie2.repository.TaskRepository;
import org.example.zadanie2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class IntegrationTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void cleanDatabase() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    protected UserDto createUser(String firstName, String lastName, String email) throws Exception {
        CreateUserDto createUserDto = new CreateUserDto(firstName, lastName, email);

        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        return objectMapper.readValue(jsonResponse, UserDto.class);
    }

    protected TaskDto createTask(String title, String description, String status, LocalDate dueDate, Set<Long> assignedUserIds) throws Exception {
        CreateTaskDto createTaskDto = new CreateTaskDto(title, description, status, dueDate, assignedUserIds);

        MvcResult result = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        return objectMapper.readValue(jsonResponse, TaskDto.class);
    }
}
