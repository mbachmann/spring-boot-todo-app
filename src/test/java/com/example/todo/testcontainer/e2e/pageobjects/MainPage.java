package com.example.todo.testcontainer.e2e.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class MainPage {

    private final WebDriver driver;
    private final By listNameField = By.id("listNameTextField");
    private final By todoListItems = By.cssSelector(".todo-list-name-item");
    private final By deleteButtons = By.cssSelector(".todo-actions a[onclick^='deleteTodoList']");
    private final By pageTitle = By.cssSelector(".page-title");
    private final By editButtons = By.cssSelector(".todo-actions a[onclick^='editTodoListName']");

    public MainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(tagName = "h1")
    public WebElement h1;

    public void enterListName(String name) {
        driver.findElement(listNameField).sendKeys(name + "\n");
    }

    public List<WebElement> getTodoLists() {
        return driver.findElements(todoListItems);
    }

    public void deleteLastTodoList() {
        List<WebElement> deleteIcons = driver.findElements(deleteButtons);
        if (!deleteIcons.isEmpty()) {
            deleteIcons.get(deleteIcons.size() - 1).click();
        }
    }

    public String getPageTitle() {
        return driver.findElement(pageTitle).getText();
    }

    public String getListNameFieldValue() {
        return driver.findElement(listNameField).getAttribute("value");
    }

    public void editLastTodoList(String newName) {
        List<WebElement> editIcons = driver.findElements(editButtons);
        if (!editIcons.isEmpty()) {
            editIcons.get(editIcons.size() - 1).click();
            WebElement inputField = driver.findElement(listNameField);
            inputField.clear();
            inputField.sendKeys(newName + "\n");
        }
    }
}