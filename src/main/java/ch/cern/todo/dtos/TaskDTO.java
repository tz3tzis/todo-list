package ch.cern.todo.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TaskDTO {
    @NotBlank(message = "Name is mandatory")
    @Size(max = 100)
    String name;

    @Size(max = 500)
    String description;

    @NotNull(message = "Deadline is mandatory")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    String deadline;

    @NotBlank(message = "Category is mandatory")
    String category;

    @NotBlank(message = "Username is mandatory")
    String username;
}
