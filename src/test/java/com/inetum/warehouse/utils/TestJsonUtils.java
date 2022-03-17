package com.inetum.warehouse.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TestJsonUtils {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String convertObjectToJson(Object object) throws IOException {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return OBJECT_MAPPER.writeValueAsString(object);
    }
}
