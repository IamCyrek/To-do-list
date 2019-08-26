package com.example.controller;

import java.util.HashMap;
import java.util.Map;

class ControllerUtils {
    static Map<String, Boolean> responseBuilder(final String s, final Boolean b) {
        Map<String, Boolean> response = new HashMap<>();
        response.put(s, b);
        return response;
    }
}
