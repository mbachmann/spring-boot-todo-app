package com.example.todo.repository;


import com.example.todo.model.TodoListName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TodoListNameRepository extends JpaRepository<TodoListName, UUID> {

}
