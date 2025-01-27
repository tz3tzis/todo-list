package ch.cern.todo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskCategoryDTO {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 100)
    String name;

    @Size(max = 500)
    String description;
}
