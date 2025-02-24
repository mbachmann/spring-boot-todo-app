package com.example.todo.rest;

import com.example.todo.model.TodoItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TodoItemControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "/api/v1";
    }

    @Test
    void testNewTodoItem() throws Exception {
        TodoItem item = new TodoItem();
        item.setItemId(null);
        item.setListId(UUID.randomUUID());
        item.setTaskName("Test Task");
        item.setDone(false);

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskName").value("Test Task"));
    }

    @Test
    void testGetItemsOfOneList() throws Exception {
        UUID listId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/list/" + listId))
                .andExpect(status().isOk());
    }

    @Test
    void testEditTodoItem() throws Exception {
        TodoItem item = new TodoItem();
        item.setItemId(1L);
        item.setTaskName("Updated Task");

        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskName").value("Updated Task"));
    }

    @Test
    void testDeleteTodoItem() throws Exception {
        long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/delete/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
