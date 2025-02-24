package com.example.todo.rest;

import com.example.todo.model.TodoListName;
import com.example.todo.repository.TodoListNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class TodoListNameControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoListNameRepository todoListNameRepository;

    private UUID testTodoListId;

    @BeforeEach
    void setup() {
        // Preload the database with test data
        TodoListName todoList = new TodoListName();
        todoList.setName("Test List");
        todoList = todoListNameRepository.save(todoList);
        testTodoListId = todoList.getId();
    }

    @Test
    void testGetAllTodoListNames() throws Exception {
        mockMvc.perform(get("/api/v1/todolist-names"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));

    }

    @Test
    void testGetTodoListById() throws Exception {
        mockMvc.perform(get("/api/v1/todolist-names/{id}", testTodoListId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listName", is("Test List")));
    }

    @Test
    void testCreateTodoListName() throws Exception {
        String json = "{\"name\": \"New List\"}";

        mockMvc.perform(post("/api/v1/todolist-names")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New List")));
    }

    @Test
    void testUpdateTodoListName() throws Exception {
        String json = "{\"name\": \"Updated List\"}";

        mockMvc.perform(put("/api/v1/todolist-names/{id}", testTodoListId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated List")));
    }

    @Test
    void testDeleteTodoListName() throws Exception {
        mockMvc.perform(delete("/api/v1/todolist-names/{id}", testTodoListId))
                .andExpect(status().isNoContent());

        // Verify it was deleted
        mockMvc.perform(get("/api/v1/todolist-names/{id}", testTodoListId))
                .andExpect(status().isNotFound());
    }
}
