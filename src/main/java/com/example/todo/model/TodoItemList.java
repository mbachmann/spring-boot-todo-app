package com.example.todo.model;

import java.time.Instant;
import java.util.UUID;

public interface TodoItemList    {
    UUID getListId();
    int getCount();
    Instant getFromDate();
    Instant getToDate();
}
