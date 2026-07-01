package com.clarivate.UserAPI.infraestructure.sourceOfTrue.dto;

import java.util.List;
import java.util.Map;

import com.clarivate.UserAPI.domain.model.User;

public record RandomUsersDTO(List<User> results, Map<String, Object> info) {

}
