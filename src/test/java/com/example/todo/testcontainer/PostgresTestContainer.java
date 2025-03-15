package com.example.todo.testcontainer;

import com.example.todo.AbstractTest;
import com.example.todo.utils.HasLogger;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.MountableFile;

@ActiveProfiles("postgres-test")
public class PostgresTestContainer extends AbstractTest implements HasLogger {


    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16");

    static {
        container
                .withCopyFileToContainer(MountableFile.forClasspathResource("postgres/schema.sql"),
                "/docker-entrypoint-initdb.d/schema.sql")
                .withUsername("todoapp-user")
                .withPassword("password")
                .withDatabaseName("db")
                .withExposedPorts(5432)
                //.waitingFor(Wait.forHealthcheck())
                .start();
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

}