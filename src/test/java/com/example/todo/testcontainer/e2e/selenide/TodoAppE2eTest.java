package com.example.todo.testcontainer.e2e.selenide;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoAppE2eTest {

    @LocalServerPort
    private Integer port;

    @RegisterExtension
    static ScreenShooterExtension screenShooterExtension =
            new ScreenShooterExtension().to("target/selenide");

    @Test
    void shouldDisplayBooks() {
        open("http://localhost:" + port + "");
        $$(By.tagName("h1")).shouldHave(CollectionCondition.size(1));
    }
}
