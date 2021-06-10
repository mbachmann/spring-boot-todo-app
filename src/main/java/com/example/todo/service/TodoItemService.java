package com.example.todo.service;


import com.example.todo.model.TodoItem;
import com.example.todo.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class TodoItemService {

    @Autowired
    private TodoItemRepository todoItemRepository;

    public TodoItem saveTodoItem(TodoItem item) {
        return todoItemRepository.save(item);
    }

    public TodoItem changeDoneStateForTodoItem(Long id) {
        TodoItem item = todoItemRepository.findByItemId(id);
        if (item != null) {
            item.setDone(!item.getDone());
            todoItemRepository.save(item);
            return item;
        }
        return null;
    }

    public Boolean deleteTodoItem(Long id) {
        TodoItem item = todoItemRepository.findById(id).orElse(null);
        if (item != null) {
            todoItemRepository.delete(item);
            return true;
        }
        return false;
    }

    public TodoItem editTodoItem(TodoItem editedItem) {
        TodoItem item = todoItemRepository.findById(editedItem.getItemId()).orElse(null);
        if (item != null) {
            item.setTaskName(editedItem.getTaskName());
            return todoItemRepository.save(item);
        }
        //Create new if we dont have.
        return todoItemRepository.save(item);
    }

    public List<TodoItem> getAllTodoItemsForListId(UUID listId) {
        return todoItemRepository.findByListId(listId);
    }

    public TodoItem getItem(Long id) {
        return todoItemRepository.findByItemId(id);
    }
}
