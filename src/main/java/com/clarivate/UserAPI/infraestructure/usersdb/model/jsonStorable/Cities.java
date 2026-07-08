package com.clarivate.UserAPI.infraestructure.usersdb.model.jsonStorable;

import com.clarivate.UserAPI.domain.model.Timezone;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cities {
    private String cityName;
    private Timezone timeZone;
    private Streets streets;
}
