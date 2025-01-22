package ch.cern.todo.repos;

import ch.cern.todo.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByCategoryName(String name);
    List<Task> findTasksByUserName(String name);
}
