package com.example.todo.testcontainer;

import com.example.todo.testcontainer.container.database.MariadbTestContainer;
import com.example.todo.testcontainer.container.database.MySQLTestContainer;
import com.example.todo.testcontainer.container.database.OracleTestContainer;
import com.example.todo.testcontainer.container.database.PostgresTestContainer;
import com.example.todo.utils.HasLogger;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.testcontainers.containers.JdbcDatabaseContainer;

import java.util.Arrays;


public class DBBaseTestContainer extends MySQLTestContainer implements HasLogger {

    @Autowired
    private Environment environment;

    public static JdbcDatabaseContainer<?> databaseContainer;


    @PostConstruct
    public void init() {

        String activeTestProfile = Arrays.stream(environment.getActiveProfiles()).filter(profile -> profile.equals("test")).findFirst().orElse(null);

        switch (activeTestProfile) {
            case "mysql-test":
                databaseContainer = MySQLTestContainer.databaseContainer;
                break;
            case "mariadb-test":
                databaseContainer = MariadbTestContainer.databaseContainer;
                break;
            case "postgres-test":
                databaseContainer = PostgresTestContainer.databaseContainer;
                break;
            case "oracle-test":
                databaseContainer = OracleTestContainer.databaseContainer;
                break;
            case null:
                getLogger().error ("No active test profile found", new Exception("No active test profile found"));
                break;
            default:
                getLogger().error ("No database test container found", new Exception("No database test container found"));
                break;
        }
    }

}
