package ch.cern.todo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Size(max = 500)
    private String description;

    @Column(nullable = false)
    @NotNull
    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private TaskCategory category;

    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    private User user;
}
