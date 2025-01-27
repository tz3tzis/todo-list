package ch.cern.todo.service;

import ch.cern.todo.entities.TaskCategory;
import ch.cern.todo.repos.TaskCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TaskCategoryServiceImplTest {

    @Mock
    private TaskCategoryRepository taskCategoryRepository;

    @InjectMocks
    private TaskCategoryServiceImpl taskCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetAllTaskCategories_thenReturnAllList() {
        TaskCategory category1 = TaskCategory.builder().id(1L).name("Work").description("Work-related tasks").build();
        TaskCategory category2 = TaskCategory.builder().id(2L).name("Personal").description("Personal tasks").build();
        when(taskCategoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));
        List<TaskCategory> categories = taskCategoryService.getAllTaskCategories();

        assertThat(categories).hasSize(2);
        assertThat(categories).contains(category1, category2);
        verify(taskCategoryRepository, times(1)).findAll();
    }

    @Test
    void whenGetTaskCategory_thenReturnOptional() {
        TaskCategory category = TaskCategory.builder().id(1L).name("Work").description("Work-related tasks").build();
        when(taskCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
        TaskCategory result = taskCategoryService.getTaskCategory(1L);

        assertThat(result).isEqualTo(category);
        verify(taskCategoryRepository, times(1)).findById(1L);
    }

    @Test
    void whenGetTaskCategory_thenThrowsExceptionWhenNotFound() {
        when(taskCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> taskCategoryService.getTaskCategory(1L)
        );
        assertThat(exception.getMessage()).isEqualTo("Task category not found");
        verify(taskCategoryRepository, times(1)).findById(1L);
    }

    @Test
    void whenUpdateTaskCategory_thenVerify() {
        TaskCategory category = TaskCategory.builder().id(1L).name("Work").description("Work-related tasks").build();
        when(taskCategoryRepository.save(category)).thenReturn(category);
        TaskCategory result = taskCategoryService.updateTaskCategory(category);

        assertThat(result).isEqualTo(category);
        verify(taskCategoryRepository, times(1)).save(category);
    }

    @Test
    void whenCreateNew_thenSaveTimesOne() {
        TaskCategory category = TaskCategory.builder().id(1L).name("Work").description("Work-related tasks").build();
        when(taskCategoryRepository.save(category)).thenReturn(category);
        taskCategoryService.createTaskCategory(category);

        verify(taskCategoryRepository, times(1)).save(category);
    }

    @Test
    void whenDeleteTaskCategory_thenDeletByIdTimesOne() {
        taskCategoryService.deleteTaskCategory(1L);
        verify(taskCategoryRepository, times(1)).deleteById(1L);
    }
}