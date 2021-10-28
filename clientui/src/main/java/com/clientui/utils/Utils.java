package com.clientui.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class Utils to write an object as JsonString
 *
 * @author Christine Duarte
 */
public class Utils {

    /**
     * Method that write an object as JsonString to build the body of the request
     * mock
     *
     * @param obj - The object that we want send in the request
     * @return The value as JsonString of the object
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("The obj does not be writting", e);
        }
    }
}

