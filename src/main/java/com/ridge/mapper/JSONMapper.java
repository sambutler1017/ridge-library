package com.ridge.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON Mapper class for handling objects in various functions.
 * 
 * @author Sam Butler
 * @since April 3, 2022
 */
public class JSONMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONMapper.class);

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
            return getMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * This will take in a json string and cast it to the corresponding class type
     * that was passed in. If an error occurs in the casting, it will return null.
     * 
     * @param <T>        The generic type of the data.
     * @param jsonString The string to convert to an object.
     * @param clazz      The class to cast the string too.
     * @return The casted object.
     */
    public static <T> T convert(String jsonString, Class<T> clazz) {
        try {
            return getMapper().readValue(jsonString, clazz);
        } catch (Exception e) {
            LOGGER.warn("Could not convert JSON string '{}' to '{}' data type.", jsonString, clazz.getSimpleName());
            System.out.println(e);
            return null;
        }
    }

    /**
     * Get the default mapper for the instance.
     * 
     * @return {@link ObjectMapper} object.
     */
    public static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
