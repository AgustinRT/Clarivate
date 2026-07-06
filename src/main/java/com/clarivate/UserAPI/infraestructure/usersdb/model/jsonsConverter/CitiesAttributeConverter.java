package com.clarivate.UserAPI.infraestructure.usersdb.model.jsonsConverter;

import com.clarivate.UserAPI.infraestructure.usersdb.model.jsonStorable.Cities;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CitiesAttributeConverter implements AttributeConverter<Cities, String> {

    ObjectMapper parser = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Cities attribute) {
        try {
            String response = parser.writeValueAsString(attribute);
            return response;
        } catch (Exception e) {

            return null;
        }
    }

    @Override
    public Cities convertToEntityAttribute(String dbData) {
        try {
            Cities citiesResponse = parser.readValue(dbData, Cities.class);
            return citiesResponse;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
