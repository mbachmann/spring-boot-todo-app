package com.example.todo.repository;

import java.util.List;
import java.util.UUID;

import com.example.todo.model.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    TodoItem findByItemId(Long itemId);
    List<TodoItem> findByListId(UUID listId);
    @Query("select distinct listId from TodoItem ")
    List<UUID> findDistinctListId();
}
