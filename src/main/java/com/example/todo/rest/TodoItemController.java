package com.example.todo.rest;

import com.example.todo.dto.TodoItemListsDTO;
import com.example.todo.dto.TodoItemsDTO;
import com.example.todo.model.TodoItem;
import com.example.todo.service.TodoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class TodoItemController {
    @Autowired
    private TodoItemService todoItemService;

    @GetMapping("/item/{itemId}")
    public TodoItem getItem(@PathVariable Long itemId) {
        return todoItemService.getItem(itemId);
    }

    // Get todo list, based on listId
    @GetMapping("/list/{listId}")
    public List<TodoItem> getItem(@PathVariable UUID listId) {
        return todoItemService.getAllTodoItemsForListId(listId);
    }

    // Get all todo listIds
    @GetMapping("/listids")
    public TodoItemListsDTO getListIDs() {
        return todoItemService.getTodoItemListIDs();
    }

    // Get all todo lists
    @GetMapping("/list")
    public List<TodoItemsDTO> getAllTodoItems() {
        return todoItemService.getTodoItemLists();
    }


    // New todo item
    @PostMapping(value = "/new")
    public ResponseEntity<TodoItem> newTodoItem(@RequestBody TodoItem item) {
        return ResponseEntity.ok(todoItemService.saveTodoItem(item));
    }

    // Edit todo item
    @PutMapping("/edit")
    public ResponseEntity<TodoItem> editTodoItem(@RequestBody TodoItem item) {
        return ResponseEntity.ok(todoItemService.editTodoItem(item));
    }

    // Delete todo item
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteTodoItem(@PathVariable Long id) {
        return ResponseEntity.ok(todoItemService.deleteTodoItem(id));
    }

    // Change done state
    @PutMapping("/state/{id}")
    public ResponseEntity<TodoItem> changeDoneState(@PathVariable Long id) {
        return ResponseEntity.ok(todoItemService.changeDoneStateForTodoItem(id));
    }
}

