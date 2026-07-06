package com.clarivate.UserAPI.domain.model;

import java.util.List;

public record ResponseUsersApi(List<User> results,
                               InfoResponse info) {

}
