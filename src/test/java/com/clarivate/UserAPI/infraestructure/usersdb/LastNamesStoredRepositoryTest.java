package com.clarivate.UserAPI.infraestructure.usersdb;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.clarivate.UserAPI.domain.model.User;
import com.clarivate.UserAPI.infraestructure.TestContainerInitializer;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.portOut.StaticUserPort;
import com.clarivate.UserAPI.infraestructure.usersdb.model.LastNamesStoredEntity;
import com.clarivate.UserAPI.infraestructure.usersdb.model.LocationsStoredEntity;
import com.clarivate.UserAPI.infraestructure.usersdb.model.jsonStorable.Cities;
import com.clarivate.UserAPI.infraestructure.usersdb.model.jsonStorable.Streets;
import com.clarivate.UserAPI.infraestructure.usersdb.repositories.LastNamesStoredRepository;
import com.clarivate.UserAPI.infraestructure.usersdb.repositories.LocationsStoredRepository;

@SpringBootTest
@TestPropertySource(properties={
    "spring.config.location=classpath:application.yaml"
})
@ActiveProfiles(value="test")
@ContextConfiguration(initializers = TestContainerInitializer.class)
public class LastNamesStoredRepositoryTest {

    @Autowired
    private StaticUserPort workUserMocks;

    @Autowired
    private LastNamesStoredRepository lastNamesStoredRepository;

    @BeforeEach
    void setUp() {
        lastNamesStoredRepository.deleteAll();
    }

    @Test
    public void saveStoredLastNamesTest() {
        // GIVEN
        int sizerequest = 1;
        User mockUser = workUserMocks.getRandomSubList(sizerequest).getFirst();
        LastNamesStoredEntity candidate = new LastNamesStoredEntity();
        candidate.setNameUser(mockUser.name().get("last"));

        // WHEN
        LastNamesStoredEntity response = lastNamesStoredRepository.save(candidate);

        // THEN
        assertEquals(candidate.getId(), response.getId());
    }

    @Test
    public void getStoredLastNamesTest() {
        // GIVEN
        int sizerequest = 1;
        User mockUser = workUserMocks.getRandomSubList(sizerequest).getFirst();
        LastNamesStoredEntity candidate = new LastNamesStoredEntity();
        candidate.setNameUser(mockUser.name().get("last"));
        lastNamesStoredRepository.save(candidate);

        LastNamesStoredEntity exampleLastName = new LastNamesStoredEntity();
        exampleLastName.setNameUser(candidate.getNameUser());

        // WHEN
        List<LastNamesStoredEntity> responseNames = lastNamesStoredRepository.findAll(Example.of(exampleLastName));

        // THEN
        assertEquals(1, responseNames.size());
        assertEquals(candidate.getId(), responseNames.getFirst().getId());
    }
}
