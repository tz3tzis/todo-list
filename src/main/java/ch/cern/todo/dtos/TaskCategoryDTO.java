package ch.cern.todo.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskCategoryDTO {
    String name;
    String description;
}
