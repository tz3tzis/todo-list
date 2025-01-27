package ch.cern.todo.controllers;

import ch.cern.todo.dtos.TaskDTO;
import ch.cern.todo.mappers.TaskMapper;
import ch.cern.todo.service.TaskServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tasks")
@Validated
@Tag(name = "Task API", description = "Endpoints for managing tasks")
public class TaskController {

    private final TaskServiceImpl taskService;
    private final TaskMapper map;

    @GetMapping
    @Operation(summary = "Get all task DTOs", description = "Retrieve a list of all task DTOs")
    List<TaskDTO> getAllTasksForUser() {
        return taskService.getAllTasksForUser()
                .stream()
                .map(map::toDto)
                .collect(toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task DTO by ID", description = "Retrieve a specific task DTO by its ID")
    TaskDTO getTask(@PathVariable long id) {
        return map.toDto(taskService.getTask(id));
    }

    @PostMapping("/create")
    @ResponseStatus(CREATED)
    @Operation(summary = "Create a new task", description = "Add a new task to the database")
    void createTask(@Valid @RequestBody TaskDTO taskDto) {
        taskService.createTask(map.toEntity(taskDto));
    }

    @PutMapping
    @ResponseStatus(OK)
    @Operation(summary = "Update a task", description = "Update all fields of a task")
    TaskDTO updateTask(@Valid @RequestBody TaskDTO taskDto) {
        return map.toDto(taskService.updateTask(map.toEntity(taskDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Delete a task", description = "Remove a task by its ID")
    void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }


    //search by parameters
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Search tasks",
            description = "Search tasks by name, description, deadline, category, or username",
            parameters = {
                    @Parameter(name = "name", description = "Search by task name (optional)", example = "Homework"),
                    @Parameter(name = "description", description = "Search by task description (optional)", example = "Complete math homework"),
                    @Parameter(name = "deadline", description = "Search by task deadline (optional). Format: yyyy-MM-dd", example = "2025-01-27"),
                    @Parameter(name = "category", description = "Search by task category name (optional)", example = "Work"),
                    @Parameter(name = "username", description = "Search by task owner's username (optional)", example = "johndoe")
            })
    List<TaskDTO> searchTasks(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String description,
                              @RequestParam(required = false) LocalDate deadline,
                              @RequestParam(required = false) String category,
                              @RequestParam(required = false) String username) {
        return taskService.searchTasks(name, description, deadline, category, username)
                .stream()
                .map(map::toDto)
                .collect(toList());
    }

}


