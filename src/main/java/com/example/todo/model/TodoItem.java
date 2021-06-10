package com.example.todo.model;

import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class TodoItem {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long itemId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID listId;

    @NotEmpty(message="* Enter Task Name")
    private String taskName;

    private Boolean isDone = false; // Default value

    private Date createdAt = new Date();

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
