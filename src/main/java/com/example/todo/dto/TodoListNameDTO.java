package com.example.todo.dto;

import java.time.Instant;
import java.util.UUID;

public class TodoListNameDTO {

    private int count;
    private UUID listId;
    private Instant fromDate;
    private Instant toDate;
    private String listName;

    public TodoListNameDTO(){}

    public TodoListNameDTO(int count, UUID listId, Instant fromDate, Instant toDate, String listName){
        this.count = count;
        this.listId = listId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.listName = listName;
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

    public Instant getFromDate() {
        return fromDate;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getToDate() {
        return toDate;
    }

    public void setToDate(Instant toDate) {
        this.toDate = toDate;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}
