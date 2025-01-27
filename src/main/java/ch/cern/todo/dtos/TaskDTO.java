package ch.cern.todo.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskDTO {
    String name;
    String description;
    LocalDate deadline;
    String category;
    String username;
}
