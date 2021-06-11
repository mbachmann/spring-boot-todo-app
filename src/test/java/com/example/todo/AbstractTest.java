package com.example.todo;

import com.example.todo.model.TodoItem;
import com.example.todo.repository.TodoItemRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractTest {
    protected MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    List<TodoItem> todoItems = new ArrayList<>();

    protected void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected void createTodoTestItems(TodoItemRepository todoItemRepository) {
        UUID uuidList1 = UUID.randomUUID();
        for (int i = 0; i < 2 ; i ++) {
            TodoItem todoItem = new TodoItem(uuidList1, "Todo List 1" + i, new Date());
            todoItems.add(todoItem);
            todoItemRepository.save(todoItem);
        }

        UUID uuidList2 = UUID.randomUUID();
        for (int i = 0; i < 2 ; i ++) {
            TodoItem todoItem = new TodoItem(uuidList2, "Todo Liste 2" + i, new Date());
            todoItems.add(todoItem);
            todoItemRepository.save(todoItem);
        }
    }

    protected void deleteTodoDoTestItems(TodoItemRepository todoItemRepository) {
        for (TodoItem todoItem : todoItems) {
            todoItemRepository.delete(todoItem);
        }
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(json, clazz);
    }

    protected String extractEmbeddedFromHalJson(String content, String attribute) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            Map<String,Object> map = new HashMap<>();
            map = mapper.readValue(content, new TypeReference<HashMap<String,Object>>(){});
            Map<String,Object> embedded = (Map<String, Object>) map.get("_embedded");
            List<Object> customers = (List<Object>) embedded.get(attribute);
            return mapToJson(customers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
