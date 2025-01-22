package ch.cern.todo.mappers;


import ch.cern.todo.entities.Task;
import ch.cern.todo.entities.TaskCategory;
import ch.cern.todo.dtos.TaskCategoryDTO;
import ch.cern.todo.repos.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskCategoryMapper {

    private final TaskRepository taskRepository;

    public TaskCategory toEntity(TaskCategoryDTO taskCategoryDto) {

        List<Task> tasks = taskRepository.findTasksByCategoryName(taskCategoryDto.getName());

        return TaskCategory.builder()
                .name(taskCategoryDto.getName())
                .description(taskCategoryDto.getDescription())
                .tasks(tasks)
                .build();
    }

    public TaskCategoryDTO toDto(TaskCategory taskCategory) {
        return TaskCategoryDTO.builder()
                .name(taskCategory.getName())
                .description(taskCategory.getDescription())
                .build();
    }
}
