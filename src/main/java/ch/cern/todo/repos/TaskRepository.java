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
                        SELECT 
                            t.id,
                            t.name,
                            t.description ,
                            t.deadline,
                            t.category_id,
                            t.app_user_id
                        FROM task t
                        LEFT JOIN task_category c ON t.category_id = c.id
                        LEFT JOIN app_user u ON t.app_user_id = u.id
                        WHERE (:name IS NULL OR t.name = :name) 
                          AND (:description IS NULL OR t.description = :description)
                          AND (:deadline IS NULL OR CAST(t.deadline AS DATE) = :deadline)
                          AND (:category IS NULL OR c.name = :category) 
                          AND (:username IS NULL OR u.name = :username)
                    \s""")
    List<Task> searchTasks(String name, String description, LocalDate deadline, String category, String username);
}
