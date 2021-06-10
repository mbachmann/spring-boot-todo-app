package com.example.todo.controller;

import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class MainController {
    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model)
    {
        // When people visit site, create an UUID for a list and return it.
        UUID uuid = UUID.randomUUID();
        model.addAttribute("listId", uuid.toString());
        return "index";
    }
}
