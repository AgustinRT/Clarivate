package com.clarivate.UserAPI.infraestructure.sourceOfTrue.portOut;

import com.clarivate.UserAPI.domain.model.ResponseUsersApi;

public interface RandomUserPort {
    ResponseUsersApi getListRandomUser(Integer numberUsers);
}
