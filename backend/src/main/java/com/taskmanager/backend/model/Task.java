package com.taskmanager.backend.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.Instant;

import lombok.Data;

@Data
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private boolean done;
    private Instant created;

    @Enumerated(EnumType.STRING)
    private Priority priority;
}
