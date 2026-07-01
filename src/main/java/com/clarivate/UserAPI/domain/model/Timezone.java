package com.clarivate.UserAPI.domain.model;

public record Timezone(String offset, String description) {
/*    public Timezone(String offset, String description) {
        //"timezone":{"offset":"-11:00","description":"Midway Island, Samoa"}
        String[] timeIndication = offset.split(":");
        LocalTime workTime = LocalTime.of(Integer.valueOf(timeIndication[0]), Integer.valueOf(timeIndication[1]));
        this(workTime, description);
    } */
}
