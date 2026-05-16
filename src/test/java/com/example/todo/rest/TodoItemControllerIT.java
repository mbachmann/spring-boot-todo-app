package com.example.todo.rest;

import com.example.todo.TodoApplication;
import com.example.todo.model.TodoItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TodoApplication.class, TodoItemControllerIT.TestConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TodoItemControllerIT {

    @Configuration
    static class TestConfig {
        @Bean
        ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper;
        }
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private String baseUrl;
    private static final UUID listId = UUID.randomUUID();
    private static long itemId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        baseUrl = "/api/v1";
    }

    @Test
    @Order(1)
    void testNewTodoItem() throws Exception {
        TodoItem item = new TodoItem();
        item.setItemId(null);
        item.setListId(listId);
        item.setTaskName("Test Task");
        item.setDone(false);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        TodoItem data = objectMapper.readValue(content, TodoItem.class);
        assert data != null;
        itemId = data.getItemId();
        assertEquals(listId, data.getListId());
        assertEquals("Test Task", data.getTaskName());
    }

    @Test
    @Order(2)
    void testGetItemsOfOneList() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/list/" + listId))
                .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        List<TodoItem> data = objectMapper.readValue(content, new TypeReference<List<TodoItem>>() {});
        assert data != null;
        assertEquals(1, data.size());
        assertEquals(listId, data.getFirst().getListId());
        assertEquals(itemId, data.getFirst().getItemId());
        assertEquals("Test Task", data.getFirst().getTaskName());
    }

    @Test
    @Order(3)
    void testEditTodoItem() throws Exception {
        TodoItem item = new TodoItem();
        item.setItemId(itemId);
        item.setListId(listId);
        item.setTaskName("Updated Task");
        item.setDone(false);

        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskName").value("Updated Task"));
    }

    @Test
    @Order(4)
    void testDeleteTodoItem() throws Exception {
        long id = itemId;
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/delete/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
