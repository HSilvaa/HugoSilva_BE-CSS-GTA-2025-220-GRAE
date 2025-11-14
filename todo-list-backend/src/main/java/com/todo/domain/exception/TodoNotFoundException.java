package com.todo.domain.exception;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(Long id) {
        super("Task not found with id " + id);
    }
}