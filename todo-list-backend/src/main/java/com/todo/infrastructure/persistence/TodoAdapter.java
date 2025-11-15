package com.todo.infrastructure.persistence;

import com.todo.domain.Todo;
import com.todo.domain.exception.TodoNotFoundException;
import com.todo.infrastructure.persistence.entity.TodoEntity;
import com.todo.infrastructure.persistence.mapper.TodoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoAdapter{

    private final TodoJpaRepository repository;

    public TodoAdapter(TodoJpaRepository repository){
        this.repository = repository;
    }

    public TodoEntity save(Todo todo){
        TodoEntity entity = TodoMapper.todoEntity(todo);
        repository.save(entity);
        return entity;
    }

    public List<TodoEntity> findAll(){
        return repository.findAll();
    }

    public TodoEntity getById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    public void deleteById(Long id){
        getById(id);
        repository.deleteById(id);
    }

}
