package com.clarivate.UserAPI.infraestructure.restApi;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import com.clarivate.UserAPI.application.portIn.ApiUserPort;
import com.clarivate.UserAPI.domain.exceptions.InvalidParamRequest;
import com.clarivate.UserAPI.domain.model.InfoResponse;
import com.clarivate.UserAPI.domain.model.ResponseUsersApi;
import com.clarivate.UserAPI.domain.model.User;
import com.clarivate.UserAPI.infraestructure.restAPI.controller.RandomUserController;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.SourceOfTrueProperties;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.dto.RandomUsersDTO;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.portOut.RandomUserPort;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.portOut.StaticUserPort;

@SpringBootTest
@TestPropertySource(properties={
    "spring.config.location=classpath:application.yaml"
})
public class RandomUserControllerTest {


    @MockitoBean
    private RestTemplate mockRest;

    @Autowired
    private RandomUserPort randomUserPort;

    @Autowired
    private SourceOfTrueProperties source;

    @Autowired
    private StaticUserPort workUserMocks;

    @Autowired
    private ApiUserPort apiUserPort;

    @Test
    public void testUserControllergetUsers() {
        // GIVEN
        int sizerequest = 6;

        RandomUserController randomUserController = new RandomUserController(apiUserPort);
        List<User> mockUsers = workUserMocks.getRandomSubList(sizerequest);
        String pathUse = String.format("%s?results=%s", source.getBasepath(), mockUsers.size());
        InfoResponse mockInfo = new InfoResponse("23a23", "1.4", mockUsers.size(), 1);
        RandomUsersDTO respUserDTOMock = new RandomUsersDTO(mockUsers, mockInfo);
        ResponseEntity<RandomUsersDTO> responseMock = ResponseEntity.ok(respUserDTOMock);
        Mockito.when(mockRest.exchange(pathUse,
                                        HttpMethod.GET,
                                        null,
                                        RandomUsersDTO.class)).thenReturn(responseMock);
        // WHEN
        ResponseEntity<ResponseUsersApi> responseUsers = randomUserController.getRandomUsers(sizerequest);

        // THEN
        assertEquals(responseUsers.getStatusCode(), HttpStatus.OK);
        assertEquals(sizerequest, responseUsers.getBody().results().size());
    }


    @Test
    public void testUserControllerBadRequest() {
        // GIVEN
        int sizerequest = 40000;
        RandomUserController randomUserController = new RandomUserController(apiUserPort);
        // WHEN / THEN
        assertThrows(InvalidParamRequest.class, () -> randomUserController.getRandomUsers(sizerequest));
    }

    @Test
    public void testUserControllergetUsersIncomplete() {
        // GIVEN
        int sizerequest = 6;

        RandomUserController randomUserController = new RandomUserController(apiUserPort);
        List<User> mockUsers = workUserMocks.getRandomSubList((sizerequest - 2));
        String pathUse = String.format("%s?results=%s", source.getBasepath(), sizerequest);
        InfoResponse mockInfo = new InfoResponse("23a23", "1.4", mockUsers.size(), 1);
        RandomUsersDTO respUserDTOMock = new RandomUsersDTO(mockUsers, mockInfo);
        ResponseEntity<RandomUsersDTO> responseMock = ResponseEntity.ok(respUserDTOMock);
        Mockito.when(mockRest.exchange(pathUse,
                                        HttpMethod.GET,
                                        null,
                                        RandomUsersDTO.class)).thenReturn(responseMock);
        // WHEN
        ResponseEntity<ResponseUsersApi> responseUsers = randomUserController.getRandomUsers(sizerequest);

        // THEN
        assertEquals(responseUsers.getStatusCode(), HttpStatus.OK);
        assertEquals(sizerequest, responseUsers.getBody().results().size());
    }

    @Test
    public void testUserControllergetUsersFailRemoteServer() {
        // GIVEN
        int sizerequest = 6;

        RandomUserController randomUserController = new RandomUserController(apiUserPort);
        List<User> mockUsers = workUserMocks.getRandomSubList(sizerequest);
        String pathUse = String.format("%s?results=%s", source.getBasepath(), mockUsers.size());
        ResponseEntity<RandomUsersDTO> responseMock = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        Mockito.when(mockRest.exchange(pathUse,
                                        HttpMethod.GET,
                                        null,
                                        RandomUsersDTO.class)).thenReturn(responseMock);
        // WHEN
        ResponseEntity<ResponseUsersApi> responseUsers = randomUserController.getRandomUsers(sizerequest);

        // THEN
        assertEquals(responseUsers.getStatusCode(), HttpStatus.OK);
        assertEquals(sizerequest, responseUsers.getBody().results().size());
    }
}
