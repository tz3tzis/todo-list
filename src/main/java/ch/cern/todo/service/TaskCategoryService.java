package ch.cern.todo.service;

import ch.cern.todo.entities.TaskCategory;

import java.util.List;

public interface TaskCategoryService {

    List<TaskCategory> getAllTaskCategories();

    TaskCategory getTaskCategory(Long id);

    TaskCategory updateTaskCategory(TaskCategory taskCategory);

    void createTaskCategory(TaskCategory taskCategory);

    void deleteTaskCategory(Long id);

}
