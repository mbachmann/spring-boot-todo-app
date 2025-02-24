package com.example.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoItemsController {

    /**
     * This method is used to return the todo-items page.
     * @param listId The listId of the todo list to get loaded.
     * @param model Model object to pass data to the view.
     * @return todo-items in order to load one-todo-items-list.html from the templates' folder.
     */
    @GetMapping("/todo-items")
    public String showTodoItemsPage(@RequestParam("listId") String listId, Model model) {
        model.addAttribute("listId", listId);
        return "one-todo-items-list"; // Serves one-todo-items-list.html
    }
}
