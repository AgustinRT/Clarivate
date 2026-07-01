package com.clarivate.UserAPI.domain.model;

import lombok.NonNull;

public record Coordinates(@NonNull Double latitude, @NonNull Double longitude) {

}
