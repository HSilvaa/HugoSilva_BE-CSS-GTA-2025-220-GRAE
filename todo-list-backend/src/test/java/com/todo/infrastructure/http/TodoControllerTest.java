package com.todo.infrastructure.http;

import com.todo.domain.DTO.TodoRequest;
import com.todo.domain.DTO.TodoResponse;
import com.todo.domain.Todo;
import com.todo.domain.exception.TodoNotFoundException;
import com.todo.domain.mapper.TodoRequestMapper;
import com.todo.infrastructure.persistence.TodoAdapter;
import com.todo.infrastructure.persistence.entity.TodoEntity;
import com.todo.infrastructure.persistence.mapper.TodoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import javassist.NotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoControllerTest {
    @Mock
    private TodoAdapter adapter;

    @InjectMocks
    private TodoController controller;

    private static TodoRequest req1;
    private static TodoRequest req2;

    private static Todo todo1;
    private static Todo todo2;

    private static TodoEntity e1;
    private static TodoEntity e2;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        req1 = new TodoRequest(1L, "task 1", 2);
        req2 = new TodoRequest(2L, "task 2", 1);

        todo1 = TodoRequestMapper.toDomain(req1);
        todo2 = TodoRequestMapper.toDomain(req2);

        e1 = TodoMapper.todoEntity(todo1);
        e2 = TodoMapper.todoEntity(todo2);
    }

    @Test
    void shouldCreateTodo() {
        when(adapter.save(any(Todo.class))).thenReturn(e1);

        ResponseEntity<TodoResponse> response = controller.create(req1);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
        assertEquals("task 1", response.getBody().getTask());
        assertEquals(2, response.getBody().getPriority());
        verify(adapter).save(any(Todo.class));
    }

    @Test
    void shouldGetById() throws Exception {
        when(adapter.getById(1L)).thenReturn(e1);

        ResponseEntity<TodoResponse> response = controller.getById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("task 1", response.getBody().getTask());
        assertEquals(2, response.getBody().getPriority());
    }

    @Test
    void shouldThrowExceptionWhenNotFound() throws Exception {
        Long id = 99L;
        when(adapter.getById(id)).thenThrow(new TodoNotFoundException(id));
        assertThrows(TodoNotFoundException.class, () -> controller.getById(id));
    }

    @Test
    void shouldReturnAll() {
        when(adapter.findAll()).thenReturn(Arrays.asList(e1, e2));

        ResponseEntity<List<TodoResponse>> response = controller.getAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("task 1", response.getBody().get(0).getTask());
        assertEquals(2, response.getBody().get(0).getPriority());
    }

    @Test
    void shouldDeleteById() {
        ResponseEntity<Void> response = controller.delete(1L);
        assertEquals(204, response.getStatusCodeValue());
        verify(adapter).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeleteNotFound() {
        Long id = 99L;
        doThrow(new TodoNotFoundException(id))
                .when(adapter).deleteById(id);
        assertThrows(TodoNotFoundException.class, () -> controller.delete(id));
    }
}