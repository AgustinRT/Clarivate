package com.clarivate.UserAPI.infraestructure;

import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Component
@Testcontainers
public class TestcontainerManager {

    private static final ReentrantLock lock = new ReentrantLock();
    private static boolean initialized = false;


    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:15")
                                            .withDatabaseName("testusersdb")
                                            .withUsername("admin")
                                            .withPassword("admin")
//                                            .withExposedPorts(5432, 5432)
                                            .withReuse(true);
//                                            .withCopyToContainer(MountableFile.forClasspathResource("init.sql"), "/docker-entrypoint-initdb.d");


    @DynamicPropertySource
    static void configurationProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }
    @BeforeAll
    public static void init(){
        lock.lock();
        try {
            if(!initialized && postgres.isRunning()) {
                initialized = true;
                postgres.start();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    @AfterAll
    public static void cleanUp(){
        lock.lock();
        try {
            if(initialized) {
                postgres.stop();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
