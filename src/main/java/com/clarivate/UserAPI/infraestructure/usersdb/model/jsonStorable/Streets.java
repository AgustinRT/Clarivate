package com.clarivate.UserAPI.infraestructure.usersdb.model.jsonStorable;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Streets {
    String postCode;
    List<String> listStreets;
}
