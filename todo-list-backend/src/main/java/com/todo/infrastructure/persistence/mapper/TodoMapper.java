package com.todo.infrastructure.persistence.mapper;

import com.todo.domain.Todo;
import com.todo.infrastructure.persistence.entity.TodoEntity;

public class TodoMapper {

    public static Todo toDomain(TodoEntity entity){
        return new Todo(entity.getId(), entity.getTask(), entity.getPriority());
    }

    public static TodoEntity todoEntity(Todo domain){
        return new TodoEntity(domain.getId(), domain.getTask(), domain.getPriority());
    }
}
