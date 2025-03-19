package com.example.todo.testcontainer;

import com.example.todo.TodoApplication;
import com.example.todo.testcontainer.container.web.WebContainer;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Testcontainers
@SpringBootTest(classes = TodoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = BaseTestContainer.Initializer.class)
public class BaseTestContainer extends DBBaseTestContainer {

    public static String testOption;
    public static String browserOption;
    public static String screenshotFolder = null;
    public static BrowserWebDriverContainer<?> webContainer;


    @Value("${test.option}")
    public void setTestOption(String option) {
        testOption = option;
    }

    @Value("${test.browser}")
    public void setBrowserOption(String browser) {
        browserOption = browser;
        webContainer = WebContainer.getWebContainer(browser);
    }

    @LocalServerPort
    private Integer port;


    protected static int screenShotNumber = 0;
    protected static String baseUrl;
    protected static WebDriver driver;


    @BeforeAll
    void beforeAll() throws MalformedURLException {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        AbstractDriverOptions<?> driverOptions = null;
        if (testOption.equals("local")) {
            switch (browserOption) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    driver = new ChromeDriver(chromeOptions);
                    driverOptions = chromeOptions.merge(capabilities);
                    break;
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    driver = new FirefoxDriver(firefoxOptions);
                    driverOptions = firefoxOptions.merge(capabilities);
                    break;
                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    driver = new EdgeDriver(edgeOptions);
                    driverOptions = edgeOptions.merge(capabilities);
                    break;
                default:
                    throw new UnsupportedOperationException("Browser not supported");
            }


        } else {
            switch (browserOption) {
                case "chrome":
                    capabilities.setBrowserName("chrome");
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--headless");

                    driverOptions = chromeOptions.merge(capabilities);
                    break;
                case "firefox":
                    capabilities.setBrowserName("firefox");
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("-headless");
                    driverOptions = firefoxOptions.merge(capabilities);
                    break;
                case "edge":
                    capabilities.setBrowserName("MicrosoftEdge");
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--headless");
                    driverOptions = edgeOptions.merge(capabilities);

                    break;
                default:
                    throw new UnsupportedOperationException("Browser not supported");
            }
            if (testOption.equals("grid")) {
                driver = new RemoteWebDriver(Paths.get("http://localhost:4444").toUri().toURL(), capabilities);
            } else {
                driver = new RemoteWebDriver(webContainer.getSeleniumAddress(), driverOptions);
                getLogger().info("Remote Webdriver Selenium Address: {}", webContainer.getSeleniumAddress());
            }
        }
        assert driverOptions != null;

        getLogger().info("Webdriver started at url: {} with browser: [{}] [{}] and test option: [{}] selected: ",
                baseUrl, driverOptions.getBrowserName(),  driverOptions.getBrowserVersion(), testOption);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        Dimension windowSize = driver.manage().window().getSize();
        getLogger().info("Tests with Window size: {}", windowSize);
        // driver.manage().window().setSize(new Dimension(1800,1100));
    }


    @AfterAll
    static void afterAll() {
        if (driver != null) {
            driver.quit();
        }
    }

    private TestInfo testInfo;

    @BeforeEach
    void init(TestInfo testInfo) {
        this.testInfo = testInfo;
    }

    @AfterEach
    void afterEach() {
        if (testInfo.getTestClass().isPresent() && testInfo.getTestMethod().isPresent()) {
            takeScreenshot(testInfo.getTestClass().get().getSimpleName(), testInfo.getTestMethod().get().getName());
        } else {
            getLogger().warn("Test class or method information is not available for screenshot.");
        }
        printBrowserLogs();
        if (driver != null) {
            // driver.quit();
        }

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
                if (screenshotFolder == null) {
                    screenshotFolder = "screenshots/selenium/" + getFormattedLocalDateTimeNow();
                }
                File localScreenshots = new File(new File("target"), screenshotFolder);
                if (!localScreenshots.exists() || !localScreenshots.isDirectory()) {
                    localScreenshots.mkdirs();
                }
                method = method.replaceAll(" ", "_");
                File screenshot = new File(localScreenshots, String.format("%04d", screenShotNumber++) + "_" + className + "_" + method + "_" + ".png");
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
        if (testOption.equals("local")) {
            baseUrl = "http://localhost:" + port;
        }
    }

    protected void printBrowserLogs() {

        try {
            Logs logs = driver.manage().logs();
            LogEntries logEntries = logs.get(LogType.BROWSER);
            logEntries.forEach(entry -> getLogger().info(entry.toString()));
        } catch (Exception e) {
            getLogger().info("Failed to get browser logs");
        }
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            applicationContext.addApplicationListener(
                    (ApplicationListener<WebServerInitializedEvent>) event -> {
                        org.testcontainers.Testcontainers.exposeHostPorts(event.getWebServer().getPort());
                        baseUrl = "http://host.testcontainers.internal:" + event.getWebServer().getPort();
                    }
            );
        }
    }

}