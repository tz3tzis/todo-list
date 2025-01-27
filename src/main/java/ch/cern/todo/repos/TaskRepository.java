package ch.cern.todo.repos;

import ch.cern.todo.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByCategoryName(String name);

    List<Task> findTasksByUserName(String name);

    @Query(nativeQuery = true,
                    value = """
                       SELECT t FROM Task t WHERE\s
                           (:name IS NULL OR t.name LIKE %:name%) AND\s
                           (:description IS NULL OR t.description LIKE %:description%) AND\s
                           (:deadline IS NULL OR t.deadline = :deadline) AND\s
                           (:category IS NULL OR t.category.name = :category) AND\s
                           (:username IS NULL OR t.user.name = :username)
                   \s""")
    List<Task> searchTasks(String name, String description, LocalDate deadline, String category, String username);
}
