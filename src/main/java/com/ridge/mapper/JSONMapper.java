package com.ridge.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON Mapper class for handling objects in various functions.
 * 
 * @author Sam Butler
 * @since April 3, 2022
 */
public class JSONMapper {

    /**
     * Will take in a custom object that will be mapped to a json string. Any null
     * or invalid fields will be set to null. If an exception occurs mapping the
     * object it will return a null string.
     * 
     * @param <T>  The type of the object.
     * @param data The data to be mapped to a json string.
     * @return {@link String} object of the mapped data.
     */
    public static <T> String mapObjectToJsonString(T data) {
        try {
            return new ObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
