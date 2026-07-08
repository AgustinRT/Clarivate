package com.clarivate.UserAPI.infraestructure;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.postgresql.PostgreSQLContainer;

public class TestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:15")
                                            .withDatabaseName("testusersdb")
                                            .withUsername("admin")
                                            .withPassword("admin")
//                                            .withExposedPorts(5432, 5432)
                                            .withReuse(true);
    static {
        postgres.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
            "spring.datasource.url=" + postgres.getJdbcUrl(),
            "spring.datasource.username=" + postgres.getUsername(),
            "spring.datasource.password=" + postgres.getPassword()
        ).applyTo(applicationContext);
    }

}
