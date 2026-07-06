package com.clarivate.UserAPI.application.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import com.clarivate.UserAPI.application.portIn.ApiUserPort;
import com.clarivate.UserAPI.domain.exceptions.UserServiceFail;
import com.clarivate.UserAPI.domain.model.InfoResponse;
import com.clarivate.UserAPI.domain.model.ResponseUsersApi;
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
    public ResponseUsersApi getRandomUserList(Integer request) {
        try {
            ResponseUsersApi candidateUsers = randomUserPort.getListRandomUser(request);
            return candidateUsers.results().size() < request ? this.getStarticUsers(request, candidateUsers.results())
                                                                : candidateUsers;
        } catch (ResourceAccessException e){
            return this.getStarticUsers(request, null);
        } catch (UserServiceFail e) {
            return this.getStarticUsers(request, null);
        }
    }

    private ResponseUsersApi getStarticUsers(Integer request, List<User> currentUsers) {
        List<User> responseUsers = currentUsers != null && !currentUsers.isEmpty() ? currentUsers : new ArrayList<>();
        responseUsers.addAll(staticUserPort.getRandomSubList((request - responseUsers.size())));
        String seed = Long.toHexString(new Date().getTime());
        return new ResponseUsersApi(responseUsers, new InfoResponse(seed, "static", responseUsers.size(), 1));
    }

}