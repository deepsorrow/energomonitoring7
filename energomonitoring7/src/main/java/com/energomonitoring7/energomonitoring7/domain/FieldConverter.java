package com.energomonitoring7.energomonitoring7.domain;

import com.google.gson.Gson;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class FieldConverter implements AttributeConverter<Field, String> {

    @Override
    public String convertToDatabaseColumn(Field field) {
        Gson gson = new Gson();
        return gson.toJson(field);
    }

    @Override
    public Field convertToEntityAttribute(String fieldString) {
        Gson gson = new Gson();
        return gson.fromJson(fieldString, Field.class);
    }

}
