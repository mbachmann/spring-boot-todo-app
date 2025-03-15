package com.example.todo.testcontainer.e2e.tests;

import com.example.todo.testcontainer.BaseTestContainer;
import com.example.todo.testcontainer.e2e.pageobjects.VersionPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Main Page e2e Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VersionPageE2ETest extends BaseTestContainer {
    private VersionPage versionPage;
    private Wait<WebDriver> wait;

    @BeforeEach
    public void setUp() {
        wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(2))  // Maximum time to wait
                .pollingEvery(Duration.ofMillis(100)) // Interval between each poll
                .ignoring(NoSuchElementException.class);

        versionPage = new VersionPage(driver);
        driver.get(baseUrl);
        versionPage.navigateToAbout(baseUrl);

    }

    @Test
    @Order(1)
    @DisplayName("I see 'Application Version Info' as a title and 'Version Information' as a page title")
    void checkTheCorrectTitle () {
        String titleAbout = "Application Version Info";
        assertThat(driver.getTitle()).isEqualTo(titleAbout);
        assertThat(versionPage.getPageTitle()).isEqualTo("Version Information");
        takeScreenshot(getClass().getSimpleName(), "checkTheCorrectTitle");
    }

    @Test
    @Order(2)
    @DisplayName("I see 'the application version', 'Spring Boot version', 'Java version', and 'SpringDoc version'")
    public void testVersionPageContents() {
        assertNotNull(versionPage.getApplicationVersion());
        assertNotNull(versionPage.getSpringBootVersion());
        assertNotNull(versionPage.getJavaVersion());
        assertNotNull(versionPage.getSpringDocVersion());
    }

    @Test
    @Order(3)
    @DisplayName("I see 'the application version value', 'Spring Boot version value', 'Java version value', and 'SpringDoc version value'")
    public void testVersionPageContentsValues() {
        assertNotNull(versionPage.getApplicationVersionValue());
        assertNotNull(versionPage.getSpringBootVersionValue());
        assertNotNull(versionPage.getJavaVersionValue());
        assertNotNull(versionPage.getSpringDocVersionValue());
    }
}

