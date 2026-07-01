package com.clarivate.UserAPI.application.adapter;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import com.clarivate.UserAPI.application.portIn.ApiUserPort;
import com.clarivate.UserAPI.domain.exceptions.UserServiceFail;
import com.clarivate.UserAPI.domain.model.Coordinates;
import com.clarivate.UserAPI.domain.model.Street;
import com.clarivate.UserAPI.domain.model.User;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.portOut.RandomUserPort;
import com.clarivate.UserAPI.infraestructure.sourceOfTrue.portOut.StaticUserPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserAdapter implements ApiUserPort {

    private final RandomUserPort randomUserPort;

    private final StaticUserPort staticUserPort;

    @Override
    public List<User> getRandomUserList(Integer request) {
        try {
            List<User> candidateUsers = randomUserPort.getListRandomUser(request);
            return candidateUsers.size() < request ? this.getStarticUsers(request, candidateUsers) : candidateUsers;
        } catch (ResourceAccessException e){
            return this.getStarticUsers(request, null);
        } catch (UserServiceFail e) {
            return this.getStarticUsers(request, null);
        }
    }

    private List<User> getStarticUsers(Integer request, List<User> currentUsers) {
        List<User> responseUsers = currentUsers != null && !currentUsers.isEmpty() ? currentUsers : new ArrayList<>();
        responseUsers.addAll(staticUserPort.getRandomSubList((request - responseUsers.size())));
        return responseUsers;
    }

}