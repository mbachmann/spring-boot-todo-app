package com.example.todo.testcontainer.e2e.pageobjects;

import com.example.todo.utils.HasLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class TodoItemsPage implements HasLogger {
    private final WebDriver driver;

    private final By backButton = By.xpath("//button[contains(text(), 'Back to Main Page')]");
    private final By taskInputField = By.id("taskNameTextField");
    private final By todoList = By.cssSelector(".todo-list");
    private final By todoRow = By.cssSelector(".todo-row");
    public final By todoListNameItems = By.cssSelector(".todo-list-name-item"); // from Main Page

    public TodoItemsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }


    public void navigateBackToMainPage() {
        driver.findElement(backButton).click();
    }

    public void navigateToTodoItemList(Wait<WebDriver> wait, String listName) {
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".todo-list-name-item"), 4));
        sleep(100);
        driver.findElement(By.xpath("//a[contains(text(), '" + listName + "')]")).click();
        wait.until(ExpectedConditions.titleIs("To-Do List"));
    }

    public void enterTask(String taskName) {
        WebElement inputField = driver.findElement(taskInputField);
        inputField.clear();
        inputField.sendKeys(taskName);
        assert getTaskNameFieldValue().equals(taskName) ;
        inputField.sendKeys(Keys.ENTER);
    }

    private String getTaskNameFieldValue() {
        return driver.findElement(taskInputField).getAttribute("value");
    }

    public boolean isTaskPresent(String taskName) {
        return !driver.findElements(By.xpath("//span[contains(text(), '" + taskName + "')]")).isEmpty();
    }

    public void deleteTask(Wait<WebDriver> wait, String taskName) {
        int listSize = getTaskListSize();
        WebElement taskElement = driver.findElement(By.xpath("//span[contains(text(), '" + taskName + "')]" +
                "/following-sibling::span/a[contains(@onclick, 'deleteTodoItem')]"));
        taskElement.click();
        wait.until(ExpectedConditions.numberOfElementsToBe(todoRow, listSize - 1));
    }

    public int getTaskListSize() {
        return driver.findElements(todoRow).size();
    }

    public boolean isTaskDeleted(String taskName) {
        return driver.findElements(By.xpath("//span[contains(text(), '" + taskName + "')]")).isEmpty();
    }

    public void editTask(String oldTaskName, String newTaskName) {
        WebElement editButton = driver.findElement(By.xpath("//span[contains(text(), '" + oldTaskName + "')]" +
                "/following-sibling::span/a[contains(@onclick, 'editTodoItem')]"));
        editButton.click();
        assert getTaskNameFieldValue().equals(oldTaskName) ;
        enterTask(newTaskName);
    }

    public void toggleTaskDone(String taskName) {
        WebElement checkbox = driver.findElement(By.xpath("//span[contains(text(), '" + taskName + "')]" +
                "/preceding-sibling::a[contains(@onclick, 'changeDoneStateFetch')]"));
        checkbox.click();
    }

    public boolean checkState(String taskName) {
        sleep(100);
        WebElement checkbox = driver.findElement(By.xpath("//span[contains(text(), '" + taskName + "')]" +
                "/preceding-sibling::a[contains(@onclick, 'changeDoneStateFetch')]"));
        WebElement li = checkbox.findElement(By.xpath("./../.."));
        if (li.getAttribute("class") != null) {
            return Objects.requireNonNull(li.getAttribute("class")).contains("completed");
        }
        return false;
    }

    private void sleep(long milliSeconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliSeconds);
        } catch (InterruptedException e) {
            getLogger().info(Arrays.toString(e.getStackTrace()));
        }
    }


}
