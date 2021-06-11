package com.example.todo.dto;

import java.util.List;
import java.util.UUID;

public class TodoItemListsDTO {

    private int count;
    private List<UUID> todoItemList;

    public TodoItemListsDTO(){}

    public TodoItemListsDTO(int count, List<UUID> todoItemList){
        this.count = count;
        this.todoItemList = todoItemList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<UUID> getTodoItemList() {
        return todoItemList;
    }

    public void setTodoItemList(List<UUID> todoItemList) {
        this.todoItemList = todoItemList;
    }
}
