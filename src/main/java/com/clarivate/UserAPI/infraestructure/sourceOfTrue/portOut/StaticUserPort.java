package com.clarivate.UserAPI.infraestructure.sourceOfTrue.portOut;

import java.util.List;

import com.clarivate.UserAPI.domain.model.User;

public interface StaticUserPort {
    List<User> getUsers();
    List<User> getRandomSubList(Integer numberUsers);
}
