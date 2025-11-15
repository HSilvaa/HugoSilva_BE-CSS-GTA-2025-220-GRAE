package com.todo.infrastructure.persistence;

import com.todo.domain.Todo;
import com.todo.domain.exception.TodoNotFoundException;
import com.todo.domain.mapper.TodoRequestMapper;
import com.todo.domain.DTO.TodoRequest;
import com.todo.infrastructure.persistence.entity.TodoEntity;
import com.todo.infrastructure.persistence.mapper.TodoMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoAdapterTest {

    @Mock
    private TodoJpaRepository repository;

    @InjectMocks
    private TodoAdapter adapter;

    private static Todo todo1;
    private static Todo todo2;

    private static TodoEntity e1;
    private static TodoEntity e2;

    @BeforeAll
    static void initData() {
        todo1 = TodoRequestMapper.toDomain(new TodoRequest(1L, "task 1", 2));
        todo2 = TodoRequestMapper.toDomain(new TodoRequest(2L, "task 2", 1));

        e1 = TodoMapper.todoEntity(todo1);
        e2 = TodoMapper.todoEntity(todo2);
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        reset(repository);
    }

    @Test
    void shouldSaveTodo() {
        when(repository.save(any())).thenReturn(e1);

        TodoEntity result = adapter.save(todo1);

        assertEquals(1L, result.getId());
        assertEquals("task 1", result.getTask());
        verify(repository).save(any());
    }

    @Test
    void shouldReturnAll() {
        when(repository.findAll()).thenReturn(Arrays.asList(e1, e2));

        List<TodoEntity> list = adapter.findAll();

        assertEquals(2, list.size());
        assertEquals("task 1", list.get(0).getTask());
    }

    @Test
    void shouldGetById() {
        when(repository.findById(1L)).thenReturn(Optional.of(e1));

        TodoEntity result = adapter.getById(1L);

        assertEquals("task 1", result.getTask());
    }

    @Test
    void shouldThrowWhenNotFound() {
        Long id = 99L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> adapter.getById(id));
    }

    @Test
    void shouldDelete() {
        when(repository.findById(1L)).thenReturn(Optional.of(e1));

        adapter.deleteById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeleteNotFound() {
        Long id = 99L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> adapter.deleteById(id));
    }
}
