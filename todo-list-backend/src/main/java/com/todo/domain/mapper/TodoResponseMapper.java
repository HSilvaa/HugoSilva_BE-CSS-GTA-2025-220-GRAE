package com.todo.domain.mapper;

import com.todo.domain.DTO.TodoResponse;
import com.todo.infrastructure.persistence.entity.TodoEntity;

import java.util.List;
import java.util.stream.Collectors;

public class TodoResponseMapper {

    public static TodoResponse toResponse(TodoEntity entity){
        return new TodoResponse(entity.getId(), entity.getTask(), entity.getPriority());
    }

    public static List<TodoResponse> toResponse(List<TodoEntity> entityList) {
        return entityList.stream()
                .map(TodoResponseMapper::toResponse)
                .collect(Collectors.toList());
    }
}
