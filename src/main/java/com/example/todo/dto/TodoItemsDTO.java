package com.example.todo.dto;

import com.example.todo.model.TodoItem;

import java.util.List;
import java.util.UUID;

public class TodoItemsDTO {

    private int count;
    private UUID listId;
    private List<TodoItem> todoItemList;

    public TodoItemsDTO(){}

    public TodoItemsDTO(int count, UUID listId, List<TodoItem> todoItemList){
        this.count = count;
        this.listId = listId;
        this.todoItemList = todoItemList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public UUID getListId() {
        return listId;
    }

    public void setListId(UUID listId) {
        this.listId = listId;
    }

    public List<TodoItem> getTodoItemList() {
        return todoItemList;
    }

    public void setTodoItemList(List<TodoItem> todoItemList) {
        this.todoItemList = todoItemList;
    }
}
