package com.clarivate.UserAPI.application.portIn;

import com.clarivate.UserAPI.domain.model.ResponseUsersApi;

public interface ApiUserPort {
    ResponseUsersApi getRandomUserList(Integer request);
}
