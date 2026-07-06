package com.clarivate.UserAPI.infraestructure.sourceOfTrue.externalRandomUser;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.clarivate.UserAPI.domain.exceptions.InvalidParamRequest;
import com.clarivate.UserAPI.domain.exceptions.UserServiceFail;
import com.clarivate.UserAPI.domain.model.InfoResponse;
import com.clarivate.UserAPI.domain.model.ResponseUsersApi;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.SourceOfTrueProperties;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.dto.RandomUsersDTO;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.portOut.RandomUserPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RandomUserRequester implements RandomUserPort {

    private final SourceOfTrueProperties source;

    private final RestTemplate userCaller;

    public ResponseUsersApi getListRandomUser(Integer numberUsers){
        if(numberUsers <= source.getMinimal() || numberUsers > source.getMax()){
            throw new InvalidParamRequest(source.getMinimal(), source.getMax());
        }

        String pathParam = String.format("%s?results=%s", source.getBasepath(), numberUsers);
        try {
            ResponseEntity<RandomUsersDTO> parsedResponse = userCaller.exchange(pathParam,
                                                                                HttpMethod.GET,
                                                                                null,
                                                                                RandomUsersDTO.class);
            if(HttpStatus.OK.equals(parsedResponse.getStatusCode())){
                ResponseUsersApi controlResponse = new ResponseUsersApi(parsedResponse.getBody().results(), new InfoResponse(parsedResponse.getBody().info().seed(),
                                                                                                                             parsedResponse.getBody().info().version(),
                                                                                                                             parsedResponse.getBody().info().results(),
                                                                                                                             parsedResponse.getBody().info().page()));
                return controlResponse;
            }else if(HttpStatus.BAD_REQUEST.equals(parsedResponse.getStatusCode())){
                throw new InvalidParamRequest(source.getMinimal(), source.getMax());
            } else {
                throw new UserServiceFail("Fail at RandomUser service");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
