package com.example.todo.testcontainer.e2e.pageobjects;

import com.example.todo.utils.HasLogger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainPage implements HasLogger {

    private final WebDriver driver;
    public final By listNameInputField = By.id("listNameTextField");
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
        WebElement inputField = driver.findElement(listNameInputField);
        inputField.sendKeys(name );
        assert getListNameFieldValue().equals(name) ;
        inputField.sendKeys(Keys.ENTER);
    }

    public void clearListNameField() {
        WebElement inputField = driver.findElement(listNameInputField);
        inputField.clear();
    }

    public void executeKeyBoardEventEnter(String script) {
        WebElement inputField = driver.findElement(listNameInputField);
        ((JavascriptExecutor) driver).executeScript(
                "var event = new KeyboardEvent('keypress', { keyCode: 13, which: 13, bubbles: true }); " +
                       "arguments[0].dispatchEvent(event);",
                inputField
        );
    }

    public String getPlaceholderOfListNameField() {
        return driver.findElement(listNameInputField).getAttribute("placeholder");
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
        return driver.findElement(listNameInputField).getAttribute("value");
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

    public void navigateToHome(Wait<WebDriver> wait, String listName) {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".todo-list-name-item"), 1));
        sleep(100);
        wait.until(ExpectedConditions.titleIs("To-Do Lists"));
    }

    private void sleep(long milliSeconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliSeconds);
        } catch (InterruptedException e) {
            getLogger().info(Arrays.toString(e.getStackTrace()));
        }
    }
}