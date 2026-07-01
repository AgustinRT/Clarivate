package com.clarivate.UserAPI.domain.model;

import java.util.Map;

import com.clarivate.UserAPI.domain.enums.Gender;

public record User(Gender gender, Map<String, String> name, Location location, String email, Login login, TimeProgresed dob, TimeProgresed registered, String phone, String cell, Map<String, String> id, Picture picture, String nat) {

}

