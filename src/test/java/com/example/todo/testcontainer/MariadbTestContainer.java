package com.example.todo.testcontainer;

import com.example.todo.AbstractTest;
import com.example.todo.utils.HasLogger;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@ActiveProfiles("mariadb-test")
public class MariadbTestContainer extends AbstractTest implements HasLogger {


    static MariaDBContainer<?> container = new MariaDBContainer<>("mariadb:11.7.2-ubi");

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

}