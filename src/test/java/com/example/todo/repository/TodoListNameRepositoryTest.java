package com.example.todo.repository;

import com.example.todo.model.TodoListName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class TodoListNameRepositoryTest {

    @Autowired
    private TodoListNameRepository todoListNameRepository;

    @Test
    void findByName_WithExistingName_ReturnsMatchingEntries() {
        String targetName = "Work";
        TodoListName first = new TodoListName(UUID.randomUUID(), targetName);
        TodoListName second = new TodoListName(UUID.randomUUID(), targetName);
        todoListNameRepository.save(first);
        todoListNameRepository.save(second);
        todoListNameRepository.save(new TodoListName(UUID.randomUUID(), "Private"));

        List<TodoListName> result = todoListNameRepository.findByName(targetName);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(item -> targetName.equals(item.getName())));
    }

    @Test
    void findByName_WithUnknownName_ReturnsEmptyList() {
        todoListNameRepository.save(new TodoListName(UUID.randomUUID(), "Shopping"));

        List<TodoListName> result = todoListNameRepository.findByName("DoesNotExist");

        assertTrue(result.isEmpty());
    }

    @Test
    void saveAndFindById_ReturnsSavedEntity() {
        UUID id = UUID.randomUUID();
        TodoListName toSave = new TodoListName(id, "Backlog");
        todoListNameRepository.save(toSave);

        Optional<TodoListName> result = todoListNameRepository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals("Backlog", result.get().getName());
    }

    @Test
    void deleteById_RemovesEntity() {
        UUID id = UUID.randomUUID();
        todoListNameRepository.save(new TodoListName(id, "ToDelete"));

        todoListNameRepository.deleteById(id);

        assertTrue(todoListNameRepository.findById(id).isEmpty());
    }
}


