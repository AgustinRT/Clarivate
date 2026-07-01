package com.clarivate.UserAPI.infraestructure.sourceOfTrue.portOut;

import java.util.List;

import com.clarivate.UserAPI.domain.model.User;

public interface RandomUserPort {
    List<User> getListRandomUser(Integer numberUsers);
}
