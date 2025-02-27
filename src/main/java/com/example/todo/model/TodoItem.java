package com.example.todo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;


@Entity
public class TodoItem {

    public TodoItem() {}

    public TodoItem(UUID listId, String taskName, Instant createdAt) {
        this.listId = listId;
        this.taskName = taskName;
        this.createdAt = createdAt;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long itemId;

    private UUID listId;

    @NotEmpty(message="* Enter Task Name")
    private String taskName;

    private Boolean isDone = false; // Default value

    private Instant createdAt = Instant.now();

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public UUID getListId() {
        return listId;
    }

    public void setListId(UUID listId) {
        this.listId = listId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
