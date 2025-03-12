package com.example.todo.testcontainer.e2e.pageobjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class MainPage {

    private final WebDriver driver;
    public final By listNameField = By.id("listNameTextField");
    public final By todoListNameItems = By.cssSelector(".todo-list-name-item");
    private final By deleteButtons = By.cssSelector(".todo-actions a[onclick^='deleteTodoList']");
    private final By pageTitle = By.cssSelector(".page-title");
    private final By editButtons = By.cssSelector(".todo-actions a[onclick^='editTodoListName']");
    private final By aboutButton = By.xpath("//button[text()='About']");

    public MainPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(tagName = "h1")
    public WebElement h1;

    public void enterListNameField(String name) {
        WebElement inputField = driver.findElement(listNameField);
        inputField.sendKeys(name);
        assert getListNameFieldValue().equals(name) ;
        inputField.sendKeys(Keys.ENTER);
    }

    public void clearListNameField() {
        WebElement inputField = driver.findElement(listNameField);
        inputField.clear();
    }

    public void executeKeyBoardEventEnter(String script) {
        WebElement inputField = driver.findElement(listNameField);
        ((JavascriptExecutor) driver).executeScript(
                "var event = new KeyboardEvent('keypress', { keyCode: 13, which: 13, bubbles: true }); " +
                       "arguments[0].dispatchEvent(event);",
                inputField
        );
    }

    public String getPlaceholderOfListNameField() {
        return driver.findElement(listNameField).getAttribute("placeholder");
    }

    public List<WebElement> getTodoLists() {
        return driver.findElements(todoListNameItems);
    }

    public WebElement getTodoListNameItemATag(String name) {

        return getTodoLists().stream()
                .filter(item -> item.getText().equals(name))
                .findFirst()
                .orElse(null);
    }

    public By getTodoListNameItemATagSelector(String name) {
        return By.xpath("//a[text()='" + name + "']");
    }


    public String getPageTitle() {
        return driver.findElement(pageTitle).getText();
    }

    public String getListNameFieldValue() {
        return driver.findElement(listNameField).getAttribute("value");
    }

    public void editTodoList(String existingName) {
        WebElement todoListRowATag = getTodoListNameItemATag(existingName);
        WebElement todoListRow = todoListRowATag.findElement(By.xpath("./.."));
        todoListRow.findElement(editButtons).click();
    }

    public void deleteTodoList(String renamedList) {
        WebElement todoListRowATag = getTodoListNameItemATag(renamedList);
        WebElement todoListRow = todoListRowATag.findElement(By.xpath("./.."));
        todoListRow.findElement(deleteButtons).click();
    }

    public void clickAboutButton() {
        driver.findElement(aboutButton).click();
    }

    public void navigateToHome(String baseUrl) {
        driver.get(baseUrl);
    }
}