package com.taskmanager.backend.service;

import com.taskmanager.backend.model.Task;
import lombok.Getter;

@Getter
public class TaskUpdateConflictException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Task has been updated by someone else.";

    private final Task currentTask;

    public TaskUpdateConflictException(Task currentTask) {
        super(DEFAULT_MESSAGE);
        this.currentTask = currentTask;
    }

}

