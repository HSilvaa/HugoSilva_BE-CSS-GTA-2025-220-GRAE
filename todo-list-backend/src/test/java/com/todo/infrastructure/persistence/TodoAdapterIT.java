package com.todo.infrastructure.persistence;

import com.todo.domain.DTO.TodoRequest;
import com.todo.domain.Todo;
import com.todo.domain.exception.TodoNotFoundException;
import com.todo.domain.mapper.TodoRequestMapper;
import com.todo.infrastructure.persistence.entity.TodoEntity;
import com.todo.infrastructure.persistence.mapper.TodoMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TodoAdapterIT {

    @Autowired
    private TodoAdapter adapter;

    @Autowired
    private TodoJpaRepository repository;

    private static Todo todo1;
    private static TodoEntity e1;

    @BeforeAll
    static void initData() {
        todo1 = TodoRequestMapper.toDomain(new TodoRequest(null, "task 1", 2));
        e1 = TodoMapper.todoEntity(todo1);
    }

    @BeforeEach
    void cleanDb() {
        repository.deleteAll();
    }

    @Test
    void shouldSaveAndRetrieve() {
        TodoEntity saved = adapter.save(todo1);

        TodoEntity found = adapter.getById(saved.getId());

        assertEquals(saved.getTask(), found.getTask());
        assertEquals(saved.getPriority(), found.getPriority());
    }

    @Test
    void shouldFindAll() {
        adapter.save(todo1);
        adapter.save(TodoRequestMapper.toDomain(new TodoRequest(null, "task 2", 1)));

        List<TodoEntity> list = adapter.findAll();

        assertEquals(2, list.size());
    }

    void shouldDelete() {
        TodoEntity saved = adapter.save(todo1);

        adapter.deleteById(saved.getId());

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldThrowWhenNotFound() {
        assertThrows(TodoNotFoundException.class, () -> adapter.getById(99L));
    }
}
