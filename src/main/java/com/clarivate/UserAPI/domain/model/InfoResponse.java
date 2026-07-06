package com.clarivate.UserAPI.domain.model;

public record InfoResponse(
    String seed,
    String version,
    Integer results,
    Integer page
) {}