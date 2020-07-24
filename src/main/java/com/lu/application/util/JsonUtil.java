package com.lu.application.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertObj2String(Object object) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JsonNode convertString2Json(String string) {
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(string);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonNode;
    }
}
