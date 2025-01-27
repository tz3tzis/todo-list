package ch.cern.todo.controllers;

import ch.cern.todo.configs.TestSecurityConfig;
import ch.cern.todo.dtos.TaskCategoryDTO;
import ch.cern.todo.entities.TaskCategory;
import ch.cern.todo.mappers.TaskCategoryMapper;
import ch.cern.todo.service.TaskCategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TaskCategoryController.class)
@WithMockUser(username = "userTest1", roles = "ADMIN")
@Import(TestSecurityConfig.class)
class TaskCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    TaskCategoryServiceImpl taskCategoryService;

    @MockitoBean
    TaskCategoryMapper taskMapper;

    List<TaskCategory> taskCategoryList;
    List<TaskCategoryDTO> taskCategoryDTOList;

    @BeforeEach
    void setUp() {
        taskCategoryList = new ArrayList<>();
        TaskCategory taskCategory1 = TaskCategory.builder().id(1L).name("Category1").build();
        TaskCategory taskCategory2 = TaskCategory.builder().id(2L).name("Category2").build();
        taskCategoryList.add(taskCategory1);
        taskCategoryList.add(taskCategory2);

        taskCategoryDTOList = new ArrayList<>();
        TaskCategoryDTO taskCategoryDTO1 = TaskCategoryDTO.builder().name("Category1").build();
        TaskCategoryDTO taskCategoryDTO2 = TaskCategoryDTO.builder().name("Category2").build();
        taskCategoryDTOList.add(taskCategoryDTO1);
        taskCategoryDTOList.add(taskCategoryDTO2);
    }


    @Test
    void shouldReturnAllTaskCategories_returnsOK() throws Exception {
        when(taskCategoryService.getAllTaskCategories()).thenReturn(taskCategoryList);
        when(taskMapper.toDto(any(TaskCategory.class))).thenReturn(taskCategoryDTOList.get(0));
        when(taskMapper.toDto(any(TaskCategory.class))).thenReturn(taskCategoryDTOList.get(1));

        mockMvc.perform(get("/api/v1/task-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(taskCategoryService, times(1)).getAllTaskCategories();
        verify(taskMapper, times(2)).toDto(any(TaskCategory.class));

    }

    @Test
    void givenTaskCategoryId_shouldGetTaskCategory_returnOK() throws Exception {
        Long id = 1L;
        when(taskCategoryService.getTaskCategory(1L)).thenReturn(taskCategoryList.get(0));
        when(taskMapper.toDto(any(TaskCategory.class))).thenReturn(taskCategoryDTOList.get(0));

        mockMvc.perform(get("/api/v1/task-categories/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Category1"));

        verify(taskCategoryService, times(1)).getTaskCategory(1L);
        verify(taskMapper, times(1)).toDto(any(TaskCategory.class));
    }

    @Test
    void givenTaskCategoryDTO_shouldCreateTaskCategory_returnCreated() throws Exception {
        TaskCategory taskCategory = taskCategoryList.getFirst();
        TaskCategoryDTO taskCategoryDTO = taskCategoryDTOList.getFirst();

        when(taskMapper.toEntity(taskCategoryDTO)).thenReturn(taskCategory);

        var body = new ObjectMapper().writeValueAsString(taskCategoryDTO);

        mockMvc.perform(post("/api/v1/task-categories/create", taskCategoryDTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        verify(taskCategoryService, times(1)).createTaskCategory(taskCategory);
        verify(taskMapper, times(1)).toEntity(taskCategoryDTO);
    }

    @Test
    void givenTaskCategoryDTO_shouldUpdateTaskCategory_returnOK() throws Exception {
        TaskCategory taskCategory = taskCategoryList.getFirst();
        TaskCategoryDTO taskCategoryDTO = taskCategoryDTOList.getFirst();

        when(taskCategoryService.updateTaskCategory(taskCategory)).thenReturn(taskCategory);
        when(taskMapper.toEntity(taskCategoryDTO)).thenReturn(taskCategory);
        when(taskMapper.toDto(taskCategory)).thenReturn(taskCategoryDTO);

        var body = new ObjectMapper().writeValueAsString(taskCategoryDTO);

        mockMvc.perform(put("/api/v1/task-categories", taskCategoryDTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Category1"));


        verify(taskCategoryService, times(1)).updateTaskCategory(taskCategory);
        verify(taskMapper, times(1)).toEntity(taskCategoryDTO);

    }

    @Test
    void shouldDeleteTaskById_returnNoContent() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/v1/task-categories/{id}", id))
                .andExpect(status().isNoContent());

        verify(taskCategoryService, times(1)).deleteTaskCategory(id);
    }
}