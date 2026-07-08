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
import com.clarivate.UserAPI.infraestructure.usersdb.model.LocationsStoredEntity;
import com.clarivate.UserAPI.infraestructure.usersdb.model.jsonStorable.Cities;
import com.clarivate.UserAPI.infraestructure.usersdb.model.jsonStorable.Streets;
import com.clarivate.UserAPI.infraestructure.usersdb.repositories.LocationsStoredRepository;

@SpringBootTest
@TestPropertySource(properties={
    "spring.config.location=classpath:application.yaml"
})
@ActiveProfiles(value="test")
@ContextConfiguration(initializers = TestContainerInitializer.class)
public class LocationsStoredRepositoryTest {

    @Autowired
    LocationsStoredRepository locationsStoredRepository;

    @Autowired
    private StaticUserPort workUserMocks;


    @BeforeEach
    void setUp() {
        locationsStoredRepository.deleteAll();
    }

    @Test
    public void saveStorerLocationsTest() {
        // GIVEN
        int sizerequest = 1;

        User mockUser = workUserMocks.getRandomSubList(sizerequest).getFirst();
        LocationsStoredEntity candidate = new LocationsStoredEntity();
        Cities cityCandidate = new Cities();
        candidate.setCountryName(mockUser.location().country());
        candidate.setStateName(mockUser.location().state());
        cityCandidate.setCityName(mockUser.location().city());
        cityCandidate.setTimeZone(mockUser.location().timezone());
        Streets streetCandidate = new Streets();
        streetCandidate.setPostCode(mockUser.location().postcode());
        streetCandidate.setListStreets(List.of(mockUser.location().street().name()));
        cityCandidate.setStreets(streetCandidate);
        candidate.setCities(cityCandidate);

        // WHEN
        LocationsStoredEntity newLocation = locationsStoredRepository.save(candidate);

        // THEN
        assertEquals(candidate.getId(), newLocation.getId());
    }

    @Test
    public void getStoredLocationsTest() {
        // GIVEN
        int sizerequest = 1;

        User mockUser = workUserMocks.getRandomSubList(sizerequest).getFirst();
        LocationsStoredEntity candidate = new LocationsStoredEntity();
        Cities cityCandidate = new Cities();
        candidate.setCountryName(mockUser.location().country());
        candidate.setStateName(mockUser.location().state());
        cityCandidate.setCityName(mockUser.location().city());
        cityCandidate.setTimeZone(mockUser.location().timezone());
        Streets streetCandidate = new Streets();
        streetCandidate.setPostCode(mockUser.location().postcode());
        streetCandidate.setListStreets(List.of(mockUser.location().street().name()));
        cityCandidate.setStreets(streetCandidate);
        candidate.setCities(cityCandidate);
        LocationsStoredEntity newLocation = locationsStoredRepository.save(candidate);

        LocationsStoredEntity exampleLocation = new LocationsStoredEntity();
        exampleLocation.setCountryName(newLocation.getCountryName());
        exampleLocation.setStateName(newLocation.getStateName());
        // WHEN
        List<LocationsStoredEntity> responseLocations = locationsStoredRepository.findAll(Example.of(exampleLocation));

        // THEN
        assertEquals(1, responseLocations.size());
        assertEquals(candidate.getId(), responseLocations.getFirst().getId());
    }
}
