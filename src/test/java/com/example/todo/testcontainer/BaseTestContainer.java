package com.example.todo.testcontainer;

import com.example.todo.AbstractTest;
import com.example.todo.TodoApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@ActiveProfiles("mysql-test")
@SpringBootTest(classes = TodoApplication.class , webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BaseTestContainer extends AbstractTest {

    protected static WebDriver browser;

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
        browser = new ChromeDriver(new ChromeOptions().addArguments("--headless"));
    }

    @AfterAll
    static void afterAll() {
        browser.quit();
    }

}