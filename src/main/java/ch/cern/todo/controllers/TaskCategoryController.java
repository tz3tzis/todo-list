package ch.cern.todo.controllers;

import ch.cern.todo.dtos.TaskCategoryDTO;
import ch.cern.todo.mappers.TaskCategoryMapper;
import ch.cern.todo.service.TaskCategoryServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/task-categories")
public class TaskCategoryController {

    private final TaskCategoryServiceImpl taskCategoryService;
    private final TaskCategoryMapper map;

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasRole('USER')")
    List<TaskCategoryDTO> getAllTaskCategories() {
        return taskCategoryService.getAllTaskCategories()
                .stream()
                .map(map::toDto)
                .collect(toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    TaskCategoryDTO getTaskCategory(@PathVariable long id) {
        return map.toDto(taskCategoryService.getTaskCategory(id));
    }

    @PostMapping("/create")
    @ResponseStatus(CREATED)
    void createTaskCategory(@Valid @RequestBody TaskCategoryDTO taskCategoryDto) {
        taskCategoryService.createTaskCategory(map.toEntity(taskCategoryDto));
    }

    @PutMapping
    @ResponseStatus(OK)
    TaskCategoryDTO updateTaskCategory(@Valid @RequestBody TaskCategoryDTO taskCategoryDto) {
        return map.toDto(taskCategoryService.updateTaskCategory(map.toEntity(taskCategoryDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    void deleteTaskCategory(@PathVariable long id) {
        taskCategoryService.deleteTaskCategory(id);
    }

}
