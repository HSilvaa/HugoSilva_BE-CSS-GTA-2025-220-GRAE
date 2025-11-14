package com.todo.domain.mapper;

import com.todo.domain.DTO.TodoRequest;
import com.todo.domain.Todo;

public class TodoRequestMapper {

    public static Todo toDomain(TodoRequest request){
        return new Todo(request.getId(), request.getTask(), request.getPriority());
    }
}
