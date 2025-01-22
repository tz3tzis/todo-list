package ch.cern.todo.controllers;

import ch.cern.todo.dtos.TaskDTO;
import ch.cern.todo.mappers.TaskMapper;
import ch.cern.todo.service.TaskServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tasks")
public class TaskController {

    private final TaskServiceImpl taskService;
    private final TaskMapper map;

    @GetMapping
    List<TaskDTO> getAllTasksForUser() {
        return taskService.getAllTasksForUser()
                .stream()
                .map(map::toDto)
                .collect(toList());
    }

    @GetMapping("/{id}")
    TaskDTO getTask(@PathVariable long id) {
        return map.toDto(taskService.getTask(id));
    }

    @PostMapping("/create")
    @ResponseStatus(CREATED)
    void createTask(@Valid @RequestBody TaskDTO taskDto) {
        taskService.createTask(map.toEntity(taskDto));
    }

    @PutMapping
    @ResponseStatus(OK)
    TaskDTO updateTask(@Valid @RequestBody TaskDTO taskDto) {
        return map.toDto(taskService.updateTask(map.toEntity(taskDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }
}
