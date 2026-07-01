package com.clarivate.UserAPI.domain.exceptions;

public class InvalidParamRequest extends IllegalArgumentException {
    public InvalidParamRequest(int minimal, int max){
        super(String.format("El numero de usuarios solicitados debe ser mayor a %s y menor a %d", minimal, max));
    }
}
