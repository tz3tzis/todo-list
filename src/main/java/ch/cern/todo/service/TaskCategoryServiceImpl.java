package ch.cern.todo.service;

import ch.cern.todo.repos.TaskCategoryRepository;
import ch.cern.todo.entities.TaskCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskCategoryServiceImpl implements TaskCategoryService {

    private final TaskCategoryRepository taskCategoryRepository;

    @Override
    public List<TaskCategory> getAllTaskCategories() {
        return taskCategoryRepository.findAll();
    }

    @Override
    public TaskCategory getTaskCategory(Long id) {
        return taskCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task category not found"));
    }

    @Override
    public TaskCategory updateTaskCategory(TaskCategory taskCategory) {
        return taskCategoryRepository.save(taskCategory);
    }

    @Override
    public void createTaskCategory(TaskCategory taskCategory) {
        taskCategoryRepository.save(taskCategory);
    }

    @Override
    public void deleteTaskCategory(Long id) {
        taskCategoryRepository.deleteById(id);
    }

}
