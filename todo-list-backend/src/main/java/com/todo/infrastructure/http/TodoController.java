package com.todo.infrastructure.http;

import com.todo.domain.DTO.TodoRequest;
import com.todo.domain.DTO.TodoResponse;
import com.todo.domain.Todo;
import com.todo.domain.mapper.TodoRequestMapper;
import com.todo.domain.mapper.TodoResponseMapper;
import com.todo.infrastructure.persistence.TodoAdapter;
import com.todo.infrastructure.persistence.entity.TodoEntity;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class TodoController {
    private final TodoAdapter adapter;

    public TodoController(TodoAdapter adapter){
        this.adapter = adapter;
    }

    @PostMapping
    public ResponseEntity<TodoResponse> create(@RequestBody TodoRequest request) {
        Todo todo = TodoRequestMapper.toDomain(request);
        TodoEntity saved = adapter.save(todo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TodoResponseMapper.toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(TodoResponseMapper.toResponse(adapter.getById(id)));
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK )
                .body(TodoResponseMapper.toResponse(adapter.findAll()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adapter.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
