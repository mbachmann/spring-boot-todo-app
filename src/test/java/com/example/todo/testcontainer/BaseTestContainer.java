package com.example.todo.testcontainer;

import com.example.todo.AbstractTest;
import com.example.todo.TodoApplication;
import com.example.todo.utils.HasLogger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@ActiveProfiles("mysql-test")
@SpringBootTest(classes = TodoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BaseTestContainer extends AbstractTest implements HasLogger {

    protected static WebDriver driver;
    protected static int screenShotNumber = 0;
    protected final String baseUrl = "http://localhost:8080";


    static MySQLContainer<?> container = new MySQLContainer<>("mysql");

    static {
        container
                .withUsername("user")
                .withPassword("password")
                .withDatabaseName("todoapp")
                .withExposedPorts(3306)
                .waitingFor(Wait.forHealthcheck())
                .start();
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        // driver = new ChromeDriver(new ChromeOptions().addArguments("--headless"));
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    }

    @AfterAll
    static void afterAll() {
        driver.quit();
    }

    protected void sleep(int milliSeconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliSeconds);
        } catch (InterruptedException e) {
            getLogger().info(Arrays.toString(e.getStackTrace()));
        }
    }

    protected void takeScreenshot(String className, String method) {
        if (driver instanceof TakesScreenshot screenshotTakingDriver) {
            try {
                File localScreenshots = new File(new File("target"), "screenshots/" + getFormattedLocalDateTimeNow());
                if (!localScreenshots.exists() || !localScreenshots.isDirectory()) {
                    localScreenshots.mkdirs();
                }

                File screenshot = new File(localScreenshots, String.format("%04d", screenShotNumber++) + "_" + className + "_" + method + "_"  + ".png");
                FileUtils.moveFile(screenshotTakingDriver.getScreenshotAs(OutputType.FILE), screenshot);
                getLogger().info("Screenshot for class={} method={} saved in: {}", className, method, screenshot.getAbsolutePath());
            } catch (Exception e1) {
                getLogger().error("Unable to take screenshot", e1);
            }
        } else {
            getLogger().info("Driver '{}' can't take screenshots so skipping it.", driver.getClass());
        }
    }

    protected static String getFormattedLocalDateTimeNow() {
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd__HH_mm");
        return timestamp.format(formatter);
    }


}