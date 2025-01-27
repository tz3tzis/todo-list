package ch.cern.todo.service;

import ch.cern.todo.entities.Task;
import ch.cern.todo.entities.User;
import ch.cern.todo.repos.TaskRepository;
import ch.cern.todo.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }


    @Test
    @SuppressWarnings("unchecked")
    void givenAdminUser_whenGetAllTasksForUser_shouldReturnAllTasks() {
        when(authentication.getName()).thenReturn("admin");
        when(authentication.getAuthorities()).thenReturn((Collection) List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        Task task1 = Task.builder().id(1L).name("Task 1").build();
        Task task2 = Task.builder().id(2L).name("Task 2").build();
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasksForUser();

        assertThat(tasks).hasSize(2).contains(task1, task2);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void givenNonAdminUser_whenGetAllTasksForUser_shouldReturnTasksForUser() {
        when(authentication.getName()).thenReturn("john");
        when(authentication.getAuthorities()).thenReturn((Collection) List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Task task1 = Task.builder().id(1L).name("Task 1").build();
        when(taskRepository.findTasksByUserName("john")).thenReturn(List.of(task1));

        List<Task> tasks = taskService.getAllTasksForUser();

        assertThat(tasks).hasSize(1).contains(task1);
        verify(taskRepository, times(1)).findTasksByUserName("john");
    }

    @Test
    void givenUser_whenCreateTask_shouldSaveTaskWithAuthenticatedUser() {
        when(authentication.getName()).thenReturn("john");
        User user = User.builder().id(1L).name("john").build();
        Task task = Task.builder().name("New Task").build();
        when(userRepository.findByName("john")).thenReturn(Optional.of(user));

        taskService.createTask(task);

        assertThat(task.getUser()).isEqualTo(user);
        verify(userRepository, times(1)).findByName("john");
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void givenTaskOwnedByUser_whenGetTask_shouldReturnTask() {
        when(authentication.getName()).thenReturn("john");
        Task task = Task.builder().id(1L).name("Task 1").user(User.builder().name("john").build()).build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTask(1L);

        assertThat(result).isEqualTo(task);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void givenTaskOwnedByAnotherUser_whenGetTask_shouldThrowAccessDeniedException() {
        when(authentication.getName()).thenReturn("john");
        Task task = Task.builder().id(1L).name("Task 1").user(User.builder().name("jane").build()).build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        org.junit.jupiter.api.Assertions.assertThrows(
                AccessDeniedException.class,
                () -> taskService.getTask(1L)
        );
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void givenTaskOwnedByUser_whenUpdateTask_shouldUpdateTask() {
        when(authentication.getName()).thenReturn("john");
        Task existingTask = Task.builder().id(1L).user(User.builder().name("john").build()).build();
        Task updatedTask = Task.builder().id(1L).name("Updated Task").build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(updatedTask)).thenReturn(updatedTask);

        Task result = taskService.updateTask(updatedTask);

        assertThat(result).isEqualTo(updatedTask);
        verify(taskRepository, times(1)).save(updatedTask);
    }

    @Test
    void givenTaskOwnedByAnotherUser_whenUpdateTask_shouldThrowAccessDeniedException() {
        when(authentication.getName()).thenReturn("john");
        Task existingTask = Task.builder().id(1L).user(User.builder().name("jane").build()).build();
        Task updatedTask = Task.builder().id(1L).name("Updated Task").build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        org.junit.jupiter.api.Assertions.assertThrows(
                AccessDeniedException.class,
                () -> taskService.updateTask(updatedTask)
        );
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void givenTaskOwnedByUser_whenDeleteTask_shouldDeleteTask() {
        when(authentication.getName()).thenReturn("john");
        Task task = Task.builder().id(1L).user(User.builder().name("john").build()).build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void givenTaskOwnedByAnotherUser_whenDeleteTask_shouldThrowAccessDeniedException() {
        when(authentication.getName()).thenReturn("john");
        Task task = Task.builder().id(1L).user(User.builder().name("jane").build()).build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        org.junit.jupiter.api.Assertions.assertThrows(
                AccessDeniedException.class,
                () -> taskService.deleteTask(1L)
        );
        verify(taskRepository, times(1)).findById(1L);
    }
}