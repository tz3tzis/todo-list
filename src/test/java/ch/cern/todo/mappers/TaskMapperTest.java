package ch.cern.todo.mappers;

import ch.cern.todo.dtos.TaskDTO;
import ch.cern.todo.entities.Task;
import ch.cern.todo.entities.TaskCategory;
import ch.cern.todo.entities.User;
import ch.cern.todo.repos.TaskCategoryRepository;
import ch.cern.todo.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


public class TaskMapperTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskCategoryRepository taskCategoryRepository;

    @InjectMocks
    private TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToDto() {
        User user = User.builder().id(1L).name("john_doe").build();
        TaskCategory category = TaskCategory.builder().id(1L).name("Work").build();
        Task task = Task.builder()
                .id(1L)
                .name("Test Task")
                .description("Test Description")
                .deadline(LocalDate.of(2025, 12, 12))
                .category(category)
                .user(user)
                .build();

        TaskDTO taskDto = taskMapper.toDto(task);

        assertThat(taskDto.getName()).isEqualTo("Test Task");
        assertThat(taskDto.getDescription()).isEqualTo("Test Description");
        assertThat(taskDto.getCategory()).isEqualTo("Work");
        assertThat(taskDto.getDeadline()).isEqualTo("2025-12-12");
        assertThat(taskDto.getUsername()).isEqualTo("john_doe");
    }

    @Test
    void testToEntity() {
        TaskDTO taskDto = TaskDTO.builder()
                .name("Test Task")
                .description("Test Description")
                .category("Work")
                .deadline("2025-12-12")
                .username("john_doe")
                .build();

        TaskCategory category = TaskCategory.builder().id(1L).name("Work").build();
        User user = User.builder().id(1L).name("john_doe").build();

        when(taskCategoryRepository.findByName("Work")).thenReturn(Optional.of(category));
        when(userRepository.findByName("john_doe")).thenReturn(Optional.of(user));

        Task task = taskMapper.toEntity(taskDto);

        assertThat(task.getName()).isEqualTo("Test Task");
        assertThat(task.getDescription()).isEqualTo("Test Description");
        assertThat(task.getCategory()).isEqualTo(category);
        assertThat(task.getDeadline()).isEqualTo(LocalDate.of(2025, 12, 12));
        assertThat(task.getUser()).isEqualTo(user);

        verify(taskCategoryRepository, times(1)).findByName("Work");
        verify(userRepository, times(1)).findByName("john_doe");
    }

    @Test
    void testToEntityThrowsExceptionWhenCategoryNotFound() {
        TaskDTO taskDto = TaskDTO.builder()
                .name("Test Task")
                .description("Test Description")
                .category("NonexistentCategory")
                .deadline("2025-12-12")
                .username("john_doe")
                .build();

        when(taskCategoryRepository.findByName("NonexistentCategory")).thenReturn(Optional.empty());

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> taskMapper.toEntity(taskDto)
        );
        assertThat(exception.getMessage()).isEqualTo("Task category not found");

        verify(taskCategoryRepository, times(1)).findByName("NonexistentCategory");
        verifyNoInteractions(userRepository);
    }
}