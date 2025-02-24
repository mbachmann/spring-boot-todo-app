package com.example.todo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
public class TodoListName {

    @NotNull
    @Id
    private UUID id;

    @NotNull
    private String name;

    public TodoListName() {
        this.id = UUID.randomUUID();
    }

    public TodoListName(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public TodoListName(String name) {
        this();
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
