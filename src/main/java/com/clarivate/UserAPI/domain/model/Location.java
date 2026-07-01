package com.clarivate.UserAPI.domain.model;

public record Location(Street street, String city, String state, String country, String postcode, Coordinates coordinates, Timezone timezone) {

    Location(Street street, String city, String state, String country, Integer postcode, Coordinates coordinates, Timezone timezone){
        this(street, city, state, country, String.valueOf(postcode), coordinates, timezone);
    }
}
