package com.clarivate.UserAPI.domain.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record TimeProgresed(LocalDateTime date, Integer age) {
    public TimeProgresed(String date, Integer age){
        LocalDateTime control = LocalDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this(control, age);
    }
    public TimeProgresed(String date, String age){
        LocalDateTime control = LocalDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this(control, Integer.valueOf(age));
    }
}