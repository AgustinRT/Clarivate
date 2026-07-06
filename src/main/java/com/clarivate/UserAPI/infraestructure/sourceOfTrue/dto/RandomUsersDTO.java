package com.clarivate.UserAPI.infraestructure.sourceOfTrue.dto;

import java.util.List;

import com.clarivate.UserAPI.domain.model.InfoResponse;
import com.clarivate.UserAPI.domain.model.User;

public record RandomUsersDTO(List<User> results, InfoResponse info) {

}
