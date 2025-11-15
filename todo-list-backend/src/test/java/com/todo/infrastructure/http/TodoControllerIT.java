package com.todo.infrastructure.http;

import com.todo.domain.DTO.TodoRequest;
import com.todo.domain.Todo;
import com.todo.domain.exception.TodoNotFoundException;
import com.todo.domain.mapper.TodoRequestMapper;
import com.todo.infrastructure.persistence.TodoAdapter;
import com.todo.infrastructure.persistence.entity.TodoEntity;
import com.todo.infrastructure.persistence.mapper.TodoMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoAdapter adapter;

    private static TodoRequest req1;
    private static TodoRequest req2;

    private static Todo todo1;
    private static Todo todo2;

    private static TodoEntity e1;
    private static TodoEntity e2;

    @BeforeAll
    static void setupCommonData() {
        req1 = new TodoRequest(1L, "task 1", 2);
        req2 = new TodoRequest(2L, "task 2", 1);

        todo1 = TodoRequestMapper.toDomain(req1);
        todo2 = TodoRequestMapper.toDomain(req2);

        e1 = TodoMapper.todoEntity(todo1);
        e2 = TodoMapper.todoEntity(todo2);
    }

    @BeforeEach
    void resetMocks() {
        reset(adapter);
    }

    // ---------- TESTS ----------

    @Test
    void shouldCreateTodo() throws Exception {
        when(adapter.save(any())).thenReturn(e1);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"task\":\"task 1\",\"priority\":2}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.task").value("task 1"))
                .andExpect(jsonPath("$.priority").value(2));
    }

    @Test
    void shouldGetById() throws Exception {
        when(adapter.getById(1L)).thenReturn(e1);

        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task").value("task 1"))
                .andExpect(jsonPath("$.priority").value(2));
    }

    @Test
    void shouldReturn404WhenNotFound() throws Exception {
        Long id = 99L;
        when(adapter.getById(id)).thenThrow(new TodoNotFoundException(id));

        mockMvc.perform(get("/" + id))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturnAll() throws Exception {
        when(adapter.findAll()).thenReturn(Arrays.asList(e1, e2));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].task").value("task 1"))
                .andExpect(jsonPath("$[1].task").value("task 2"));
    }

    @Test
    void shouldDelete() throws Exception {
        mockMvc.perform(delete("/1"))
                .andExpect(status().isNoContent());

        verify(adapter).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeleteNotFound() throws Exception {
        Long id = 99L;

        doThrow(new TodoNotFoundException(id))
                .when(adapter).deleteById(id);

        mockMvc.perform(delete("/" + id))
                .andExpect(status().isInternalServerError());
    }
}
