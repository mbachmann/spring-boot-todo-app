package com.example.todo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoItemsController.class)
class TodoItemsControllerTest {

    @Test
    void testShowTodoItemsPage() throws Exception {
        // Arrange
        TodoItemsController controller = new TodoItemsController();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        String listId = "123";
        Model model = new ConcurrentModel();

        // Act
        String viewName = controller.showTodoItemsPage(listId, model);

        // Assert
        assertThat(viewName).isEqualTo("one-todo-items-list");
        assertThat(model.getAttribute("listId")).isEqualTo(listId);
    }

    @Test
    void testShowTodoItemsPage_MockMvc() throws Exception {
        // Arrange
        TodoItemsController controller = new TodoItemsController();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // Act & Assert
        mockMvc.perform(get("/todo-items").param("listId", "123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("listId"))
                .andExpect(model().attribute("listId", "123"))
                .andExpect(view().name("one-todo-items-list"));
    }
}
