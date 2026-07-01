package com.clarivate.UserAPI.domain.exceptions;

import org.springframework.web.client.RestClientException;

public class UserServiceFail extends RestClientException {
    public UserServiceFail(String message) {
        super(message);
    }
}
