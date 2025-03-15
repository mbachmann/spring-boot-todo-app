package com.example.todo.testcontainer;

import com.example.todo.TodoApplication;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = TodoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTestContainer extends DBBaseTestContainer {

    @LocalServerPort
    private Integer port;

    protected static WebDriver driver;
    protected static int screenShotNumber = 0;
    protected  String baseUrl;

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
                method = method.replaceAll(" ", "_");
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

    @PostConstruct
    public void init() {
        baseUrl = "http://localhost:" + port;
    }


}