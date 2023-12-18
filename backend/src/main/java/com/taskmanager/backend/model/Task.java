package com.taskmanager.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.Instant;
import lombok.Data;
import lombok.Getter;

@Data
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name must not be empty.")
    @Size(min = 1, max = 200, message = "Name must be between 1 and 200 characters long.")
    private String name;

    private boolean done;

    @Getter
    @Column(updatable = false)
    private Instant created = Instant.now();

    @NotNull(message = "Priority must not be null.")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    // Enum for Priority
    public enum Priority {
        LOW, MEDIUM, HIGH
    }
}
