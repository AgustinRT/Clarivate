package com.clarivate.UserAPI.application.portIn;

import java.util.List;

import com.clarivate.UserAPI.domain.model.User;

public interface ApiUserPort {
    List<User> getRandomUserList(Integer request);
}
