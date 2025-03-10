package com.example.todo.testcontainer.e2e.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.todo.testcontainer.BaseTestContainer;
import com.example.todo.testcontainer.e2e.pageobjects.MainPage;

import org.junit.jupiter.api.*;

@DisplayName("Application")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoAppE2ETests extends BaseTestContainer {

    // private AddPage addPage;

    // private DetailPage detailPage;

    private MainPage mainPage;

    @BeforeEach
    void beforeEach() {

        mainPage = new MainPage(browser);
        browser.get(baseUrl);
    }

    @Test
    @Order(1)
    @DisplayName("I see 'To-Do Lists' as a title")
    void homePageNoResults() {
        assertThat(browser.getTitle()).isEqualTo("To-Do Lists");
        assertThat(mainPage.getPageTitle()).isEqualTo("To-Do Lists");
    }

}
