package ch.cern.todo.service;

import ch.cern.todo.entities.Task;

import java.util.List;


public interface TaskService {

    List<Task> getAllTasksForUser();

    void createTask(Task task);

    Task getTask(Long id);

    Task updateTask(Task task);

    void deleteTask(Long id);

}
