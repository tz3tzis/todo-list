package ch.cern.todo.controllers;

import ch.cern.todo.configs.TestSecurityConfig;
import ch.cern.todo.dtos.TaskDTO;
import ch.cern.todo.entities.Task;
import ch.cern.todo.entities.TaskCategory;
import ch.cern.todo.mappers.TaskMapper;
import ch.cern.todo.service.TaskServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TaskController.class)
@WithMockUser(username = "userTest1", roles = "ADMIN")
@Import(TestSecurityConfig.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    TaskServiceImpl taskService;

    @MockitoBean
    TaskMapper taskMapper;

    private List<Task> tasks;
    private List<TaskDTO> taskDTOs;

    @BeforeEach
    void setUp() {
        // Create a list of tasks
        tasks = new ArrayList<>();
        Task task1 = Task.builder().id(1L).name("Task1")
                .category(TaskCategory.builder().name("Category1").build())
                .build();
        Task task2 = Task.builder().id(2L).name("Task2")
                .category(TaskCategory.builder().name("Category2").build())
                .build();
        tasks.add(task1); tasks.add(task2);

        // Create a list of taskDTOs
        taskDTOs = new ArrayList<>();
        TaskDTO taskDTO1 = TaskDTO.builder() .name("Task1").category("Category1").build();
        TaskDTO taskDTO2 = TaskDTO.builder().name("Task2").category("Category2").build();
        taskDTOs.add(taskDTO1); taskDTOs.add(taskDTO2);
    }


    @Test
    void shouldReturnAllTasks_returnsOK() throws Exception {

        when(taskService.getAllTasksForUser()).thenReturn(tasks);
        when(taskMapper.toDto(tasks.get(0))).thenReturn(taskDTOs.get(0));
        when(taskMapper.toDto(tasks.get(1))).thenReturn(taskDTOs.get(1));

        this.mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(taskService, times(1)).getAllTasksForUser();
        verify(taskMapper, times(2)).toDto(any(Task.class));
    }


    @Test
    void givenTaskId_shouldGetTask_returnOK() throws Exception {
        long taskId = 1L;
        Task task = tasks.getFirst();
        TaskDTO taskDTO = taskDTOs.getFirst();

        Mockito.when(taskService.getTask(taskId)).thenReturn(task);
        Mockito.when(taskMapper.toDto(task)).thenReturn(taskDTO);

        this.mockMvc.perform(get("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{name: \"Task1\", category: \"Category1\"}"));


        verify(taskService, times(1)).getTask(taskId);
        verify(taskMapper, times(1)).toDto(any(Task.class));
    }

    @Test
    void givenTaskDTO_shouldCreateTask_returnCreated() throws Exception {
        TaskDTO taskDto = taskDTOs.getFirst();
        Task task = tasks.getFirst();

        Mockito.when(taskMapper.toDto(task)).thenReturn(taskDto);

        var body = new ObjectMapper().writeValueAsString(taskDto);

        mockMvc.perform(post("/api/v1/tasks/create", taskDto)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());

        taskService.createTask(task);

        verify(taskService, times(1)).createTask(task);
        verify(taskMapper, times(1)).toEntity(any(TaskDTO.class));

    }

    @Test
    void givenTaskDTO_shouldUpdateTask_returnOK() throws Exception {
        TaskDTO taskDTO = taskDTOs.getFirst();
        Task task = tasks.getFirst();

        Mockito.when(taskService.updateTask(task)).thenReturn(task);
        Mockito.when(taskMapper.toDto(task)).thenReturn(taskDTO);
        Mockito.when(taskMapper.toEntity(taskDTO)).thenReturn(task);


        var body = new ObjectMapper().writeValueAsString(taskDTO);

        mockMvc.perform(put("/api/v1/tasks", taskDTO)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());

        verify(taskService, times(1)).updateTask(task);
        verify(taskMapper, times(1)).toDto(any(Task.class));

    }

    @Test
    void shouldDeleteTaskById_returnNoContent() throws Exception {
        long taskId = 1L;
        Mockito.doNothing().when(taskService).deleteTask(taskId);
        mockMvc.perform(delete("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(taskId);
    }

}