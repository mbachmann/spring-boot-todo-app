package com.example.todo.repository;

import com.example.todo.model.TodoItem;
import com.example.todo.model.TodoItemList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

    TodoItem findByItemId(Long itemId);

    List<TodoItem> findByListId(UUID listId);

    @Query("select distinct listId from TodoItem ")
    List<UUID> findDistinctListId();

    void deleteByListId(UUID listId);

    @Query("select  t.listId as listId, count(t.itemId) as count, min(t.createdAt) as fromDate, max(t.createdAt) as toDate  from TodoItem t where t.listId is not null group by t.listId")
    List<TodoItemList> getAllTodoListDetails();

    @Query("select  t.listId as listId, count(t.itemId) as count, min(t.createdAt) as fromDate, max(t.createdAt) as toDate  from TodoItem t where t.listId = :listId group by t.listId")
    Optional<TodoItemList> findTodoListDetailsByListId(UUID listId);
}
