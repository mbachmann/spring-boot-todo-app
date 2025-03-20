package com.example.todo.testcontainer.container.web;

import com.example.todo.TodoApplication;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(classes = TodoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebContainer {

    @Container
    public static BrowserWebDriverContainer<?> webContainer;

    private static boolean isWebContainerStarted = false;

    public static BrowserWebDriverContainer<?> startAndGetWebContainer(String browser) {

        if (isWebContainerStarted) {
            return webContainer;
        } else {
            webContainer = new BrowserWebDriverContainer<>();
            return switch (browser) {
                case "chrome" -> {
                    webContainer
                            .withCapabilities(new ChromeOptions())
                            // .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("build"))
                            //.dependsOn(databaseContainer)
                            //.withNetwork(databaseContainer.getNetwork())
                            .withEnv("SCREEN_WIDTH", "1920")
                            .withEnv("SCREEN_HEIGHT", "1080")
                            .withAccessToHost(true)
                            .start();
                    isWebContainerStarted = true;
                    yield webContainer;
                }
                case "firefox" -> {
                    webContainer
                            .withCapabilities(new FirefoxOptions())
                            // .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("build"))
                            //.dependsOn(databaseContainer)
                            //.withNetwork(databaseContainer.getNetwork())
                            .withAccessToHost(true)
                            .start();
                    isWebContainerStarted = true;
                    yield webContainer;
                }
                case "edge" -> {
                    webContainer
                            .withCapabilities(new EdgeOptions())
                            // .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("build"))
                            //.dependsOn(databaseContainer)
                            //.withNetwork(databaseContainer.getNetwork())
                            .withAccessToHost(true)
                            .start();
                    isWebContainerStarted = true;
                    yield webContainer;
                }
                default -> null;
            };
        }
    }

}

