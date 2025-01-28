package ch.cern.todo.service;

import ch.cern.todo.entities.Task;
import ch.cern.todo.entities.User;
import ch.cern.todo.exception.ResourceNotFoundException;
import ch.cern.todo.repos.TaskRepository;
import ch.cern.todo.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public List<Task> getAllTasksForUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return taskRepository.findAll();
        } else {
            return taskRepository.findTasksByUserName(username);
        }
    }

    @Override
    public void createTask(Task task) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User: " + username + " not found"));
        task.setUser(user);
        taskRepository.save(task);
    }


    @Override
    public Task getTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id: " + id + " not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!task.getUser().getName().equals(username) && !isAdmin) {
            throw new AccessDeniedException("You are not authorized to view this task");
        }

        return task;
    }


    @Override
    public Task updateTask(Task task) {
        Task existingTask = taskRepository.findById(task.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task with id: " + task.getId() + " not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!existingTask.getUser().getName().equals(username) && !isAdmin) {
            throw new AccessDeniedException("You are not authorized to update this task");
        }

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task wit id: " + id + " not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!task.getUser().getName().equals(username) && !isAdmin) {
            throw new AccessDeniedException("You are not authorized to delete this task");
        }

        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> searchTasks(String name, String description, LocalDate deadline, String category, String username) {
        return taskRepository.searchTasks(name, description, deadline, category, username);
    }
}
