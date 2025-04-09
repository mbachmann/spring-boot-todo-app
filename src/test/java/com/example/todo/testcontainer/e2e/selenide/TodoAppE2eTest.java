package com.example.todo.testcontainer.e2e.selenide;
/*
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.example.todo.testcontainer.BaseTestContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Selenide.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoAppE2eTest extends BaseTestContainer {

    @RegisterExtension
    static ScreenShooterExtension screenShooterExtension =
            new ScreenShooterExtension().to("target/screenshots/selenide");

    @Test
    void shouldDisplayMainPage() {
        // open("http://localhost:" + port + "");
        WebDriverRunner.setWebDriver(driver);
        open(baseUrl);
        $$(By.tagName("h1")).shouldHave(CollectionCondition.size(1));
        screenshot("MainPage");
        takeScreenshot(getClass().getSimpleName(), "shouldDisplayMainPage");
        sleep(2000);
    }
}
*/