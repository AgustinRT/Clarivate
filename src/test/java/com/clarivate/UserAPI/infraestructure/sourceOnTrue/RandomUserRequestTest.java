package com.clarivate.UserAPI.infraestructure.sourceOnTrue;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.clarivate.UserAPI.domain.exceptions.InvalidParamRequest;
import com.clarivate.UserAPI.domain.exceptions.UserServiceFail;
import com.clarivate.UserAPI.domain.model.InfoResponse;
import com.clarivate.UserAPI.domain.model.ResponseUsersApi;
import com.clarivate.UserAPI.domain.model.User;
import com.clarivate.UserAPI.infraestructure.TestContainerInitializer;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.SourceOfTrueProperties;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.dto.RandomUsersDTO;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.externalRandomUser.RandomUserRequester;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.portOut.StaticUserPort;


@SpringBootTest
@TestPropertySource(properties={
    "spring.config.location=classpath:application.yaml"
})
@ActiveProfiles(value="test")
@ContextConfiguration(initializers = TestContainerInitializer.class)
public class RandomUserRequestTest {

    @MockitoBean
    private RestTemplate mockRest;

    @Autowired
    private SourceOfTrueProperties source;

    @Autowired
    private StaticUserPort workUserMocks;

    @Test
    public void testgetRandomUsers() {
        // GIVEN
        Integer sizeCall = 3;
        List<User> mockUsers = workUserMocks.getRandomSubList(sizeCall);
        RandomUserRequester objetTestable = new RandomUserRequester(source, mockRest);
        this.managerUserMock(mockUsers);
        
        // WHEN
        ResponseUsersApi response = objetTestable.getListRandomUser(sizeCall);

        // THEN
        assertTrue(sizeCall == response.results().size());
        assertEquals(mockUsers.get(0).email(), response.results().get(0).email());
    }

    @Test
    public void testgetRandomUsersFailRequestParams() {
        // GIVEN
        Integer sizeCall = -5;
        RandomUserRequester objetTestable = new RandomUserRequester(source, new RestTemplate());

        // WHEN / THEN
        assertThrows(InvalidParamRequest.class, () -> objetTestable.getListRandomUser(sizeCall));
    }

    @Test
    public void testgetFailParameters() {
        // GIVEN
        Integer sizeCall = 2;
        RandomUserRequester objetTestable = new RandomUserRequester(source, mockRest);
        String pathUse = String.format("%s?results=%s", source.getBasepath(), sizeCall);
        ResponseEntity<RandomUsersDTO> responseMock = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        Mockito.when(mockRest.exchange(pathUse,
                                        HttpMethod.GET,
                                        null,
                                        RandomUsersDTO.class)).thenReturn(responseMock);

        // WHEN / THEN
        assertThrows(InvalidParamRequest.class, () -> objetTestable.getListRandomUser(sizeCall));
    }

    @Test
    public void testgetFailExternalService() {
        // GIVEN
        Integer sizeCall = 2;
        RandomUserRequester objetTestable = new RandomUserRequester(source, mockRest);
        String pathUse = String.format("%s?results=%s", source.getBasepath(), sizeCall);
        ResponseEntity<RandomUsersDTO> responseMock = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        Mockito.when(mockRest.exchange(pathUse,
                                        HttpMethod.GET,
                                        null,
                                        RandomUsersDTO.class)).thenReturn(responseMock);

        // WHEN / THEN
        assertThrows(UserServiceFail.class, () -> objetTestable.getListRandomUser(sizeCall));
    }

    @Test
    public void testgetFailExternalServiceÇFailRemoteAccess() {
        // GIVEN
        Integer sizeCall = 2;
        RandomUserRequester objetTestable = new RandomUserRequester(source, mockRest);
        String pathUse = String.format("%s?results=%s", source.getBasepath(), sizeCall);

        ResourceAccessException e = new ResourceAccessException("testing error");
        Mockito.when(mockRest.exchange(pathUse,
                                        HttpMethod.GET,
                                        null,
                                        RandomUsersDTO.class)).thenThrow(e);

        // WHEN / THEN
        assertThrows(ResourceAccessException.class, () -> objetTestable.getListRandomUser(sizeCall));
    }

    private void managerUserMock(List<User> mockUsers){
        String pathUse = String.format("%s?results=%s", source.getBasepath(), mockUsers.size());
        InfoResponse mockInfo = new InfoResponse("23a23", "1.4", mockUsers.size(), 1);
        RandomUsersDTO respUserDTOMock = new RandomUsersDTO(mockUsers, mockInfo);
        ResponseEntity<RandomUsersDTO> responseMock = ResponseEntity.ok(respUserDTOMock);
        Mockito.when(mockRest.exchange(pathUse,
                                        HttpMethod.GET,
                                        null,
                                        RandomUsersDTO.class)).thenReturn(responseMock);
    }
}
