package com.example.todo.service;


import com.example.todo.dto.TodoItemListsDTO;
import com.example.todo.dto.TodoItemsDTO;
import com.example.todo.model.TodoItem;
import com.example.todo.model.TodoItemList;
import com.example.todo.model.TodoListName;
import com.example.todo.repository.TodoItemRepository;
import com.example.todo.repository.TodoListNameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TodoItemService {

    private final TodoItemRepository todoItemRepository;
    private final TodoListNameRepository todoListNameRepository;

    @Autowired
    public TodoItemService(TodoItemRepository todoItemRepository, TodoListNameRepository todoListNameRepository) {
        this.todoItemRepository = todoItemRepository;
        this.todoListNameRepository = todoListNameRepository;
    }

    public TodoItem saveTodoItem(TodoItem item) {

        Optional<TodoListName> tln = todoListNameRepository.findById(item.getListId());
        if (tln.isEmpty()) {
            long count = todoListNameRepository.count();
            String formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .withZone(ZoneId.systemDefault())
                    .format(Instant.now());
            todoListNameRepository.save(new TodoListName(item.getListId(), "New List " + (count + 1) + " from " + formattedDate));
        }
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

    /**
     * Return a DTO containing a list of UUID's and the count
     *
     * @return the TodoItemListsDTO
     */
    public TodoItemListsDTO getTodoItemListIDs() {
        List<UUID> listIds = todoItemRepository.findDistinctListId();
        return new TodoItemListsDTO(listIds.size(), listIds);
    }

    /**
     * Return a DTO containing a list of todoLists including a count, name, from and to Date
     *
     * @return the List<TodoItemsDTO>
     */
    public List<TodoItemsDTO> getTodoItemLists() {
        List<TodoItemsDTO> todoItemsDTOs = new ArrayList<>();
        List<TodoItem> todoItems = todoItemRepository.findAll();
        List<TodoItemList> todoListDetails = todoItemRepository.getAllTodoListDetails();
        List<TodoListName> todoListNames = todoListNameRepository.findAll();

        todoListNames.forEach(todoListName -> {
            List<TodoItem> todoItemsForOneList = todoItems.stream().filter(ti -> ti.getListId().equals(todoListName.getId())).toList();
            todoListDetails.stream().filter(til -> til.getListId().equals(todoListName.getId())).findFirst()
                    .ifPresentOrElse(todoListDetail -> {
                        todoItemsDTOs.add(new TodoItemsDTO(
                                todoItemsForOneList.size(),
                                todoListDetail.getListId(),
                                todoListDetail.getFromDate(),
                                todoListDetail.getToDate(),
                                todoListName.getName(),
                                todoItemsForOneList));
                    }, () -> {
                        todoItemsDTOs.add(new TodoItemsDTO(
                                todoItemsForOneList.size(),
                                todoListName.getId(),
                                null,
                                null,
                                todoListName.getName(),
                                todoItemsForOneList));
                    });
        });
        return todoItemsDTOs;
    }

    /**
     * Return a DTO containing a list of todoLists including a count, name, from and to Date
     *
     * @return the TodoItemsDTO
     */
    public TodoItemsDTO getTodoItemList(UUID listId) {
        TodoItemsDTO todoItemsDTO = new TodoItemsDTO();
        todoListNameRepository.findById(listId).ifPresent(todoListName -> {
            List<TodoItem> todoItems = todoItemRepository.findByListId(listId);
            todoItemRepository.findTodoListDetailsByListId(listId).ifPresentOrElse(todoListDetail -> {
                todoItemsDTO.setCount(todoItems.size());
                todoItemsDTO.setListId(listId);
                todoItemsDTO.setFromDate(todoListDetail.getFromDate());
                todoItemsDTO.setToDate(todoListDetail.getToDate());
                todoItemsDTO.setListName(todoListName.getName());
                todoItemsDTO.setTodoItemList(todoItems);

            }, () -> {
                todoItemsDTO.setCount(todoItems.size());
                todoItemsDTO.setListId(listId);
                todoItemsDTO.setFromDate(null);
                todoItemsDTO.setToDate(null);
                todoItemsDTO.setListName(todoListName.getName());
                todoItemsDTO.setTodoItemList(todoItems);
            });
        });
        return todoItemsDTO;
    }


    public TodoItem getItem(Long id) {
        return todoItemRepository.findById(id).orElse(null);
        // return todoItemRepository.findByItemId(id);
    }

    @Transactional
    public void deleteByListId(UUID listId) {
        todoItemRepository.deleteByListId(listId);
    }
}
