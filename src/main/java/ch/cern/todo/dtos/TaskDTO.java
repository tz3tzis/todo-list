package ch.cern.todo.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskDTO {
    String name;
    String description;
    LocalDateTime deadline;
    String category;
    String username;
}
