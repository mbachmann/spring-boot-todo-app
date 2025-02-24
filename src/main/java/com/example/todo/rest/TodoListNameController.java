package com.example.todo.rest;


import com.example.todo.dto.TodoItemsDTO;
import com.example.todo.dto.TodoListNameDTO;
import com.example.todo.model.TodoListName;
import com.example.todo.repository.TodoListNameRepository;
import com.example.todo.service.TodoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/todolist-names")
public class TodoListNameController {


    private final TodoListNameRepository todoListNameRepository;
    private final TodoItemService todoItemService;

    @Autowired
    public TodoListNameController(TodoListNameRepository todoListNameRepository, TodoItemService todoItemService) {
        this.todoListNameRepository = todoListNameRepository;
        this.todoItemService = todoItemService;
    }

    @GetMapping
    public List<TodoListNameDTO> getAllTodoListNames() {
        List<TodoItemsDTO> todoItemsDTOs =  todoItemService.getTodoItemLists();

        return todoItemsDTOs.stream()
                .map(tid -> new TodoListNameDTO(
                        tid.getCount(), tid.getListId(), tid.getFromDate(), tid.getToDate(), tid.getListName())
                ).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoListNameDTO> getTodoListNameById(@PathVariable UUID id) {

        TodoItemsDTO todoItemsDTO =  todoItemService.getTodoItemList(id);
        if (todoItemsDTO == null || todoItemsDTO.getListId() == null) {
            return ResponseEntity.notFound().build();
        } else {
            TodoListNameDTO todoListNameDTO = new TodoListNameDTO(
                    todoItemsDTO.getCount(), todoItemsDTO.getListId(), todoItemsDTO.getFromDate(), todoItemsDTO.getToDate(), todoItemsDTO.getListName()
            );
            return ResponseEntity.ok(todoListNameDTO);
        }

    }

    @PostMapping
    public TodoListName createTodoListName(@RequestBody TodoListName todoListName) {
        return todoListNameRepository.save(todoListName);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoListName> updateTodoListName(@PathVariable UUID id, @RequestBody TodoListName todoListNameDetails) {
        Optional<TodoListName> todoListName = todoListNameRepository.findById(id);
        if (todoListName.isPresent()) {
            TodoListName updatedTodoListName = todoListName.get();
            updatedTodoListName.setName(todoListNameDetails.getName());
            return ResponseEntity.ok(todoListNameRepository.save(updatedTodoListName));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoListName(@PathVariable UUID id) {
        Optional<TodoListName> todoListName = todoListNameRepository.findById(id);
        if (todoListName.isPresent()) {
            todoItemService.deleteByListId(id);
            todoListNameRepository.delete(todoListName.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}