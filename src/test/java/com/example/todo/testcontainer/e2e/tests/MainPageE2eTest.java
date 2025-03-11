package com.example.todo.testcontainer.e2e.tests;

import static org.assertj.core.api.Assertions.assertThat;


import com.example.todo.testcontainer.BaseTestContainer;
import com.example.todo.testcontainer.e2e.pageobjects.MainPage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.List;

@DisplayName("Application")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MainPageE2eTest extends BaseTestContainer {

    private final String newList = "New To-Do List";
    String renamedList = "Renamed To-Do List";
    private MainPage mainPage;
    private  Wait<WebDriver> wait;

    @BeforeEach
    public void setUp() {
        wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(2))  // Maximum time to wait
                .pollingEvery(Duration.ofMillis(100)) // Interval between each poll
                .ignoring(NoSuchElementException.class);

        mainPage = new MainPage(driver);
        driver.get(baseUrl);
    }

    @AfterEach
    public void tearDown() {
        // sleep(1);
    }

    @Test
    @Order(1)
    @DisplayName("I see 'To-Do Lists' as a title")
    void checkTheCorrectTitle () {
        mainPage.navigateToHome();
        assertThat(driver.getTitle()).isEqualTo("To-Do Lists");
        assertThat(mainPage.getPageTitle()).isEqualTo("To-Do Lists");
        assertThat(mainPage.h1.getText()).isEqualTo("To-Do Lists");
        assertThat(mainPage.getPlaceholderOfListNameField()).isEqualTo("Input new List Name then tap Enter to add");
        takeScreenshot(getClass().getSimpleName(), "checkTheCorrectTitle");
    }

    @Test
    @Order(2)
    @DisplayName("I see 4 'To-Do Lists' and their names")
    void mainPageTodoLists() {
        mainPage.navigateToHome();
        assertThat(mainPage.getTodoLists().size()).isEqualTo(4);
        assertThat(mainPage.getTodoListNameItemATag("To-Do List for business").getText()).isEqualTo("To-Do List for business");
        assertThat(mainPage.getTodoListNameItemATag("To-Do List for homework").getText()).isEqualTo("To-Do List for homework");
        assertThat(mainPage.getTodoListNameItemATag("To-Do List for private").getText()).isEqualTo("To-Do List for private");
        assertThat(mainPage.getTodoListNameItemATag("An empty To-Do List").getText()).isEqualTo("An empty To-Do List");
    }

    @Test
    @Order(3)
    @DisplayName("I can add a 'New To-Do List'")
    void mainPageAddANewList() {
        mainPage.navigateToHome();
        mainPage.enterListNameField(newList);
        wait.until(ExpectedConditions.numberOfElementsToBe(mainPage.todoListItems, 5));
        assertThat(mainPage.getTodoLists().size()).isEqualTo(5);

        List<WebElement> lists = mainPage.getTodoLists();
        assertThat(lists.stream().anyMatch(el -> el.getText().equals(newList))).isTrue();
        takeScreenshot(getClass().getSimpleName(), "mainPageAddANewList");

    }

    @Test
    @Order(4)
    @DisplayName("I can rename the 'New To-Do List'")
    void mainPageRenameTheNewListToRenamed() {

        assertThat(mainPage.getTodoLists().size()).isEqualTo(5);
        assertThat(mainPage.getTodoListNameItemATag(newList).getText()).isEqualTo(newList);

        mainPage.editTodoList(newList);
        wait.until(ExpectedConditions.attributeContains(mainPage.listNameField, "value", newList));

        mainPage.clearListNameField();
        mainPage.enterListNameField(renamedList);

        wait.until(ExpectedConditions.visibilityOfElementLocated(mainPage.getTodoListNameItemATagSelector(renamedList)));

        List<WebElement> lists = mainPage.getTodoLists();
        assertThat(lists.stream().anyMatch(el -> el.getText().equals(renamedList))).isTrue();
        takeScreenshot(getClass().getSimpleName(), "mainPageRenameTheNewListToRenamed");
    }

    @Test
    @Order(4)
    @DisplayName("I can navigate to the 'Renamed To-Do List'")
    void mainPageNavigateToRenamedList()  {
        String titleTodoItems = "To-Do List";
        mainPage.navigateToHome();
        mainPage.getTodoListNameItemATag(renamedList).click();
        wait.until(ExpectedConditions.titleIs(titleTodoItems));
        assertThat(driver.getTitle()).isEqualTo(titleTodoItems);
        takeScreenshot(getClass().getSimpleName(), "mainPageNavigateToRenamedList");
    }

    @Test
    @Order(5)
    @DisplayName("I can delete the 'Renamed To-Do List'")
    void mainPageDeleteRenamedList() {
        mainPage.deleteTodoList(renamedList);
        wait.until(ExpectedConditions.numberOfElementsToBe(mainPage.todoListItems, 4));
        assertThat(mainPage.getTodoLists().size()).isEqualTo(4);
        takeScreenshot(getClass().getSimpleName(), "mainPageDeleteRenamedList");
    }

    @Test
    @Order(6)
    @DisplayName("I can navigate to the 'About Page'")
    void mainPageNavigateToAbout()  {
        String titleAbout = "Application Version Info";
        mainPage.clickAboutButton();
        wait.until(ExpectedConditions.titleIs(titleAbout));
        assertThat(driver.getTitle()).isEqualTo(titleAbout);
        takeScreenshot(getClass().getSimpleName(), "mainPageNavigateToAbout");
    }
}
