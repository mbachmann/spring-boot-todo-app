package com.example.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@Controller
public class MainController {

    /**
     * This method is used to return the index page.
     * @param model Model object to pass data to the view.
     * @return index in order to load index-template.html from the templates' folder.
     */
    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model)
    {
        // When people visit site, create an UUID for a list and return it.
        UUID uuid = UUID.randomUUID();
        model.addAttribute("listId", uuid.toString());
        return "index-template";
    }
}
