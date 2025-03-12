package com.example.todo.testcontainer.e2e.tests;

import com.example.todo.testcontainer.BaseTestContainer;
import com.example.todo.testcontainer.e2e.pageobjects.TodoItemsPage;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class TodoItemsE2eTest extends BaseTestContainer {

    private Wait<WebDriver> wait;
    private TodoItemsPage todoItemsPage;

    @BeforeEach
    public void setUp() {
        wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(2))     // Maximum time to wait
                .pollingEvery(Duration.ofMillis(100))   // Interval between each poll
                .ignoring(NoSuchElementException.class);

        todoItemsPage = new TodoItemsPage(driver);
        driver.get(baseUrl);
        todoItemsPage.navigateToTodoItemList(wait, "To-Do List for business");
    }


    @Test
    public void testAddEditToggleAndDeleteTask() {
        String taskName = "Test Task";
        String updatedTaskName = "Updated Task";

        // Add Task
        todoItemsPage.enterTask(taskName);
        assertTrue(todoItemsPage.isTaskPresent(taskName), "Task should be added");

        // Edit Task
        todoItemsPage.editTask(taskName, updatedTaskName);
        assertTrue(todoItemsPage.isTaskPresent(updatedTaskName), "Task should be updated");

        // Toggle Done State
        todoItemsPage.toggleTaskDone(updatedTaskName);
        assertTrue(todoItemsPage.isTaskPresent(updatedTaskName), "Task should be updated");
        assertTrue(todoItemsPage.checkState(updatedTaskName), "Task should be done");

        // Toggle Done State
        todoItemsPage.toggleTaskDone(updatedTaskName);
        assertTrue(todoItemsPage.isTaskPresent(updatedTaskName), "Task should be updated");
        assertFalse(todoItemsPage.checkState(updatedTaskName), "Task should be undone");

        // Delete Task
        int listSize = todoItemsPage.getTaskListSize();
        todoItemsPage.deleteTask(wait, updatedTaskName);
        assertEquals(todoItemsPage.getTaskListSize(), listSize - 1, "Task should be deleted");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            // driver.quit();
        }
    }
}
