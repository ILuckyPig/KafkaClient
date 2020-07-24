package com.lu.application;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private static Map<String, Object> controllers = new HashMap<>();

    public static Map<String, Object> getControllers() {
        return controllers;
    }

    public static void setControllers(Map<String, Object> controllers) {
        Context.controllers = controllers;
    }

    public static <T> T getController(Class<T> clazz) {
        return (T) controllers.get(clazz.getSimpleName());
    }
}
