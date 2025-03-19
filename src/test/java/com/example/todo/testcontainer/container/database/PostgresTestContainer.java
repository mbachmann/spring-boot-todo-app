package com.example.todo.testcontainer.container.database;

import com.example.todo.AbstractTest;
import com.example.todo.utils.HasLogger;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

@ActiveProfiles("postgres-test")
public class PostgresTestContainer extends AbstractTest implements HasLogger {


    public static PostgreSQLContainer<?> databaseContainer = new PostgreSQLContainer<>("postgres:16");

    static {
        databaseContainer
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

        registry.add("spring.datasource.url", databaseContainer::getJdbcUrl);
        registry.add("spring.datasource.username", databaseContainer::getUsername);
        registry.add("spring.datasource.password", databaseContainer::getPassword);
    }

}