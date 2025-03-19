package com.example.todo.testcontainer.container.database;

import com.example.todo.AbstractTest;
import com.example.todo.utils.HasLogger;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.oracle.OracleContainer;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles({"test", "oracle-test"})
public class OracleTestContainer extends AbstractTest implements HasLogger {

    public static OracleContainer databaseContainer = new OracleContainer(DockerImageName.parse("gvenzl/oracle-free:23-slim-faststart"));

    static {
        databaseContainer
                //.withCopyFileToContainer(MountableFile.forClasspathResource("oracle/init-users.sql"),
                //"/opt/oracle/scripts/startup/init_users.sql")
                .withUsername("demouser")
                .withPassword("password")
                //.withDatabaseName("db")
                .withExposedPorts(1521, 5500)
                //.waitingFor(Wait.forListeningPort())
                .start();
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", databaseContainer::getJdbcUrl);
        registry.add("spring.datasource.username", databaseContainer::getUsername);
        registry.add("spring.datasource.password", databaseContainer::getPassword);
    }

}