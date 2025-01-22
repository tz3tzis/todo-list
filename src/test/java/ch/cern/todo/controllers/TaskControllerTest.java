package ch.cern.todo.controllers;

import ch.cern.todo.dtos.TaskDTO;
import ch.cern.todo.entities.Task;
import ch.cern.todo.entities.TaskCategory;
import ch.cern.todo.entities.User;
import ch.cern.todo.mappers.TaskMapper;
import ch.cern.todo.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskServiceImpl taskService;

    @MockitoBean
    private TaskMapper taskMapper;


    private List<Task> tasks;
    private List<TaskDTO> taskDTOs;

    @BeforeEach
    void setUp() {

        tasks = new ArrayList<>();

        Task task1 = Task.builder()
                .id(1L)
                .name("Task1")
                .category(TaskCategory.builder().name("Category1").build())
                .deadline(LocalDateTime.now().plusDays(2))
                .user(User.builder().name("userTest1").build())
                .build();

        Task task2 = Task.builder()
                .id(2L)
                .name("Task2")
                .category(TaskCategory.builder().name("Category2").build())
                .deadline(LocalDateTime.now().plusDays(2))
                .user(User.builder().name("userTest2")
                        .roles(List.of(Role.builder().name("ROLE_USER").build()))
                        .build())
                .build();

        tasks.add(task1);
        tasks.add(task2);

        taskDTOs = new ArrayList<>();

        TaskDTO taskDTO1 = TaskDTO.builder()
                .name("Task1")
                .category("Category1")
                .deadline(LocalDateTime.now().plusDays(2))
                .username("userTest1")
                .build();

        TaskDTO taskDTO2 = TaskDTO.builder()
                .name("Task2")
                .category("Category2")
                .deadline(LocalDateTime.now().plusDays(2))
                .username("userTest2")
                .build();

        taskDTOs.add(taskDTO1);
        taskDTOs.add(taskDTO2);

    }


    @Test
    void givenUser_whenGetAllTasksForUser_thenReturnsTasks() throws Exception {
        // Mock service and mapper behavior
        when(taskService.getAllTasksForUser()).thenReturn(tasks);
        when(taskMapper.toDto(tasks.get(0))).thenReturn(taskDTOs.get(0));
        when(taskMapper.toDto(tasks.get(1))).thenReturn(taskDTOs.get(1));

        // Perform the GET request and assert the results
        this.mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Task 1")))
                .andExpect(jsonPath("$[0].description", is("Description 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Task 2")))
                .andExpect(jsonPath("$[1].description", is("Description 2")));

        // Verify interactions with service and mapper
        verify(taskService, times(1)).getAllTasksForUser();
        verify(taskMapper, times(2)).toDto(any(Task.class));
    }
}