package com.clarivate.UserAPI.infraestructure.sourceOfTrue;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "userapi")
@Getter
@Setter
public class SourceOfTrueProperties {
    private String basepath;
    private int minimal;
    private int max;
}
