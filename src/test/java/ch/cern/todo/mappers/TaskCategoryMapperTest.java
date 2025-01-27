package ch.cern.todo.mappers;

import ch.cern.todo.dtos.TaskCategoryDTO;
import ch.cern.todo.entities.Task;
import ch.cern.todo.entities.TaskCategory;
import ch.cern.todo.repos.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TaskCategoryMapperTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskCategoryMapper taskCategoryMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToEntity() {
        Task task1 = Task.builder().id(1L).name("Task 1").build();
        Task task2 = Task.builder().id(2L).name("Task 2").build();

        TaskCategoryDTO taskCategoryDto = TaskCategoryDTO.builder()
                .name("Work")
                .description("Work-related tasks")
                .build();

        when(taskRepository.findTasksByCategoryName("Work")).thenReturn(List.of(task1, task2));

        TaskCategory taskCategory = taskCategoryMapper.toEntity(taskCategoryDto);

        assertThat(taskCategory.getName()).isEqualTo("Work");
        assertThat(taskCategory.getDescription()).isEqualTo("Work-related tasks");
        assertThat(taskCategory.getTasks()).containsExactly(task1, task2);

        verify(taskRepository, times(1)).findTasksByCategoryName("Work");
    }

    @Test
    void testToDto() {
        TaskCategory taskCategory = TaskCategory.builder()
                .name("Personal")
                .description("Personal tasks")
                .tasks(List.of())
                .build();

        TaskCategoryDTO taskCategoryDto = taskCategoryMapper.toDto(taskCategory);

        assertThat(taskCategoryDto.getName()).isEqualTo("Personal");
        assertThat(taskCategoryDto.getDescription()).isEqualTo("Personal tasks");
    }

    @Test
    void testToEntityWhenNoTasks() {
        TaskCategoryDTO taskCategoryDto = TaskCategoryDTO.builder()
                .name("EmptyCategory")
                .description("No tasks in this category")
                .build();

        when(taskRepository.findTasksByCategoryName("EmptyCategory")).thenReturn(List.of());

        TaskCategory taskCategory = taskCategoryMapper.toEntity(taskCategoryDto);

        assertThat(taskCategory.getName()).isEqualTo("EmptyCategory");
        assertThat(taskCategory.getDescription()).isEqualTo("No tasks in this category");
        assertThat(taskCategory.getTasks()).isEmpty();

        verify(taskRepository, times(1)).findTasksByCategoryName("EmptyCategory");
    }
}