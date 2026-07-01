package com.clarivate.UserAPI.application;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.clarivate.UserAPI.application.adapter.UserAdapter;
import com.clarivate.UserAPI.domain.exceptions.InvalidParamRequest;
import com.clarivate.UserAPI.domain.model.User;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.SourceOfTrueProperties;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.dto.RandomUsersDTO;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.portOut.RandomUserPort;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.portOut.StaticUserPort;

@SpringBootTest
@TestPropertySource(properties={
    "spring.config.location=classpath:application.yaml"
})
public class UserAdapterTest {


    @MockitoBean
    private RestTemplate mockRest;

    @Autowired
    private RandomUserPort randomUserPort;

    @Autowired
    private SourceOfTrueProperties source;

    @Autowired
    private StaticUserPort workUserMocks;

    @Test
    public void testAdapterUserGetRandomUsers() {
        // GIVEN
        int sizeRequest = 4;
        List<User> mockUsers = workUserMocks.getRandomSubList(sizeRequest);
        UserAdapter userAdapter = new UserAdapter(randomUserPort, workUserMocks);

        String pathUse = String.format("%s?results=%s", source.getBasepath(), mockUsers.size());
        RandomUsersDTO respUserDTOMock = new RandomUsersDTO(mockUsers, Map.of());
        ResponseEntity<RandomUsersDTO> responseMock = ResponseEntity.ok(respUserDTOMock);
        Mockito.when(mockRest.exchange(pathUse,
                                        HttpMethod.GET,
                                        null,
                                        RandomUsersDTO.class)).thenReturn(responseMock);

        // WHEN
        List<User> responUsers = userAdapter.getRandomUserList(sizeRequest);

        // THEN
        assertTrue(sizeRequest == responUsers.size());
    }

    @Test
    public void testAdapterUserGetRandomUsersFailrequest() {
        // GIVEN
        int sizeRequest = 40000;
        UserAdapter userAdapter = new UserAdapter(randomUserPort, workUserMocks);
        // WHEN / THEN
        assertThrows(InvalidParamRequest.class, () -> userAdapter.getRandomUserList(sizeRequest));
    }

    @Test
    public void testAdapterUserGetRandomUsersIncomplete() {
        // GIVEN
        int sizeRequest = 4;
        List<User> mockUsers = workUserMocks.getRandomSubList((sizeRequest - 2));
        UserAdapter userAdapter = new UserAdapter(randomUserPort, workUserMocks);

        String pathUse = String.format("%s?results=%s", source.getBasepath(), sizeRequest);
        RandomUsersDTO respUserDTOMock = new RandomUsersDTO(mockUsers, Map.of());
        ResponseEntity<RandomUsersDTO> responseMock = ResponseEntity.ok(respUserDTOMock);
        Mockito.when(mockRest.exchange(pathUse,
                                        HttpMethod.GET,
                                        null,
                                        RandomUsersDTO.class)).thenReturn(responseMock);

        // WHEN
        List<User> responUsers = userAdapter.getRandomUserList(sizeRequest);

        // THEN
        assertTrue(sizeRequest == responUsers.size());
    }

    @Test
    public void testAdapterUserGetRandomUsersFailRemote() {
        // GIVEN
        int sizeRequest = 4;
        List<User> mockUsers = workUserMocks.getRandomSubList(sizeRequest);
        UserAdapter userAdapter = new UserAdapter(randomUserPort, workUserMocks);
        String pathUse = String.format("%s?results=%s", source.getBasepath(), mockUsers.size());

        ResponseEntity<RandomUsersDTO> responseMock = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        Mockito.when(mockRest.exchange(pathUse,
                                        HttpMethod.GET,
                                        null,
                                        RandomUsersDTO.class)).thenReturn(responseMock);

        // WHEN
        List<User> responUsers = userAdapter.getRandomUserList(sizeRequest);

        // THEN
        assertTrue(sizeRequest == responUsers.size());
    }

    @Test
    public void testAdapterUserGetRandomUsersFailRemoteAccess() {
        // GIVEN
        int sizeRequest = 4;
        List<User> mockUsers = workUserMocks.getRandomSubList(sizeRequest);
        UserAdapter userAdapter = new UserAdapter(randomUserPort, workUserMocks);
        String pathUse = String.format("%s?results=%s", source.getBasepath(), mockUsers.size());
        ResourceAccessException e = new ResourceAccessException("testing error");
        Mockito.when(mockRest.exchange(pathUse,
                                        HttpMethod.GET,
                                        null,
                                        RandomUsersDTO.class)).thenThrow(e);

        // WHEN
        List<User> responUsers = userAdapter.getRandomUserList(sizeRequest);

        // THEN
        assertTrue(sizeRequest == responUsers.size());
    }
}
