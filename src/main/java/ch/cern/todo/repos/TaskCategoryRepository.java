package ch.cern.todo.repos;

import ch.cern.todo.entities.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {
    Optional<TaskCategory> findByName(String category);

    boolean existsByName(String name);
}
