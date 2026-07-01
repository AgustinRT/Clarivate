package com.clarivate.UserAPI.domain.model;

import lombok.NonNull;

public record Street( Integer number, @NonNull String name) {

}
