package ch.cern.todo.integration;


import ch.cern.todo.entities.Task;
import ch.cern.todo.entities.TaskCategory;
import ch.cern.todo.entities.User;
import ch.cern.todo.repos.TaskCategoryRepository;
import ch.cern.todo.repos.TaskRepository;
import ch.cern.todo.repos.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

        User user1 = userRepository.save(User.builder().name("user1").password("pass1").build());

        TaskCategory taskCat1 = taskCategoryRepository.save(TaskCategory.builder().name("Category 1").build());
        TaskCategory taskCat2 = taskCategoryRepository.save(TaskCategory.builder().name("Category 2").build());

        taskRepository.save(Task.builder()
                .name("Task 1")
                .deadline(LocalDate.parse("2025-01-27"))
                .category(taskCat1)
                .user(user1)
                .build());

        taskRepository.save(Task.builder()
                .name("Task 2")
                .deadline(LocalDate.parse("2025-01-27"))
                .category(taskCat2)
                .user(user1)
                .build());
    }

    @AfterEach
    void tearDown() {
        taskCategoryRepository.deleteAll();
        userRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    public void shouldReturnAllTasks() throws Exception {
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Task 1"))
                .andExpect(jsonPath("$[1].name").value("Task 2"));
    }


    @Test
    @WithMockUser(username = "user1", roles = "USER")
    public void givenTwoTasks_afterDelete_shouldReturnOne() throws Exception {

        // test that there are two tasks
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        //delete one task
        Long id = taskRepository.findAll().getFirst().getId();
        mockMvc.perform(delete("/api/v1/tasks/{id}", id))
                .andExpect(status().isNoContent());

        //end up with one task
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

}
