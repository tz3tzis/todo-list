package ch.cern.todo.mappers;


import ch.cern.todo.dtos.TaskDTO;
import ch.cern.todo.entities.Task;
import ch.cern.todo.entities.TaskCategory;
import ch.cern.todo.entities.User;
import ch.cern.todo.repos.TaskCategoryRepository;
import ch.cern.todo.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskMapper {

    private final UserRepository userRepository;
    private final TaskCategoryRepository taskCategoryRepository;

    public TaskDTO toDto(Task task) {
        return TaskDTO.builder()
                .name(task.getName())
                .description(task.getDescription())
                .category(task.getCategory().getName())
                .username(task.getUser().getName())
                .build();
    }


    public Task toEntity(TaskDTO taskDto) {

        TaskCategory taskCategory = taskCategoryRepository.findByName(taskDto.getCategory())
                .orElseThrow(() -> new RuntimeException("Task category not found"));

        User user = userRepository.findByName(taskDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return Task.builder()
                .name(taskDto.getName())
                .description(taskDto.getDescription())
                .category(taskCategory)
                .user(user)
                .build();
    }
}
