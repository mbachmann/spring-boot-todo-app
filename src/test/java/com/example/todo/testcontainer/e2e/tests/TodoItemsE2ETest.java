package com.example.todo.testcontainer.e2e.tests;

import com.example.todo.testcontainer.BaseTestContainer;
import com.example.todo.testcontainer.e2e.pageobjects.TodoItemsPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoItemsE2ETest extends BaseTestContainer {

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

    @Order(1)
    @Test
    public void testTasksAreLoaded() {
        assertTrue(todoItemsPage.isTaskPresent("Create a project plan"), "Task Create a project plan should be present");
        ((JavascriptExecutor) driver).executeScript("console.log('hello javascript console log');");
    }


    @Order(2)
    @Test
    public void testAddEditToggleAndDeleteTask() {
        String taskName = "Test Task";
        String updatedTaskName = "Updated Task";
        takeScreenshot(getClass().getSimpleName(), "To-Do List for business");

        // Add Task
        todoItemsPage.enterTask(taskName);

        takeScreenshot(getClass().getSimpleName(), "Task should be entered");
        assertTrue(todoItemsPage.isTaskPresent(taskName), "Task should be added");
        takeScreenshot(getClass().getSimpleName(), "Task should be added");

        // Edit Task
        todoItemsPage.editTask(taskName, updatedTaskName);
        assertTrue(todoItemsPage.isTaskPresent(updatedTaskName), "Task should be updated");
        takeScreenshot(getClass().getSimpleName(), "Task should be updated");

        // Toggle Done State
        todoItemsPage.toggleTaskDone(updatedTaskName);
        assertTrue(todoItemsPage.isTaskPresent(updatedTaskName), "Task should be updated");
        assertTrue(todoItemsPage.checkState(updatedTaskName), "Task should be done");
        takeScreenshot(getClass().getSimpleName(), "Task should be done");

        // Toggle Done State
        todoItemsPage.toggleTaskDone(updatedTaskName);
        assertTrue(todoItemsPage.isTaskPresent(updatedTaskName), "Task should be updated");
        assertFalse(todoItemsPage.checkState(updatedTaskName), "Task should be undone");
        takeScreenshot(getClass().getSimpleName(), "Task should be undone");

        // Delete Task
        int listSize = todoItemsPage.getTaskListSize();
        todoItemsPage.deleteTask(wait, updatedTaskName);
        assertEquals(todoItemsPage.getTaskListSize(), listSize - 1, "Task should be deleted");
    }

}
