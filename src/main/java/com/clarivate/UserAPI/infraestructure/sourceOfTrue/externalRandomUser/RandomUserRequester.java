package com.clarivate.UserAPI.infraestructure.sourceOfTrue.externalRandomUser;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.clarivate.UserAPI.domain.exceptions.InvalidParamRequest;
import com.clarivate.UserAPI.domain.exceptions.UserServiceFail;
import com.clarivate.UserAPI.domain.model.User;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.SourceOfTrueProperties;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.dto.RandomUsersDTO;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.portOut.RandomUserPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RandomUserRequester implements RandomUserPort {

    private final SourceOfTrueProperties source;

    private final RestTemplate userCaller;
/*
    public RandomUserRequester(SourceOfTrueProperties source, RestTemplate userCaller) {
        this.source = source;
        this.userCaller = userCaller;
    }

    public RandomUserRequester(SourceOfTrueProperties source) {
        this.source = source;
        this.userCaller = new RestTemplate();
    }*/

    public List<User> getListRandomUser(Integer numberUsers){
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
                return parsedResponse.getBody().results();
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
