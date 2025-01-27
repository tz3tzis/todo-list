package ch.cern.todo.controllers;

import ch.cern.todo.dtos.TaskCategoryDTO;
import ch.cern.todo.mappers.TaskCategoryMapper;
import ch.cern.todo.service.TaskCategoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/task-categories")
@Validated
@Tag(name = "Task Category API", description = "Endpoints for managing task categories")
public class TaskCategoryController {

    private final TaskCategoryServiceImpl taskCategoryService;
    private final TaskCategoryMapper map;

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all task category DTOs", description = "Retrieve a list of all task category DTOs")
    List<TaskCategoryDTO> getAllTaskCategories() {
        return taskCategoryService.getAllTaskCategories()
                .stream()
                .map(map::toDto)
                .collect(toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @Operation(summary = "Get task category DTO by ID", description = "Retrieve a task category DTO by ID")
    TaskCategoryDTO getTaskCategory(@PathVariable long id) {
        return map.toDto(taskCategoryService.getTaskCategory(id));
    }

    @PostMapping("/create")
    @ResponseStatus(CREATED)
    @Operation(summary = "Create a new task category ", description = "Add a new task category to the database")
    void createTaskCategory(@Valid @RequestBody TaskCategoryDTO taskCategoryDto) {
        taskCategoryService.createTaskCategory(map.toEntity(taskCategoryDto));
    }

    @PutMapping
    @ResponseStatus(OK)
    @Operation(summary = "Update a task category", description = "Update all fields of a task category")
    TaskCategoryDTO updateTaskCategory(@Valid @RequestBody TaskCategoryDTO taskCategoryDto) {
        return map.toDto(taskCategoryService.updateTaskCategory(map.toEntity(taskCategoryDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a task category", description = "Remove a task category by its ID")
    void deleteTaskCategory(@PathVariable long id) {
        taskCategoryService.deleteTaskCategory(id);
    }

}
