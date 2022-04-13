package com.ridge.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Api client class for consuming restful endpoints.
 * 
 * @author Sam Butler
 * @since April 8, 2022
 */
public class ApiClient {
    private String BASE_URL;

    private ObjectMapper objectMapper;

    private HttpClient httpClient;

    private String AUTH;

    public ApiClient() {
        this.BASE_URL = "";
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newHttpClient();
    }

    public ApiClient(String url) {
        this.BASE_URL = url;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Method for setting the authorization on requests being sent.
     * 
     * @param auth The authorization
     */
    public void setAuthorization(String auth) {
        this.AUTH = auth;
    }

    /**
     * This will do a get on the passed in API. It will then cast the results to the
     * passed in object.
     * 
     * @param api   The endpoint to hit
     * @param clazz The class to cast it too.
     * @throws Exception
     */
    public <T> T get(String api, Class<T> clazz) throws Exception {
        // create a request
        var request = getBuilder(api).GET().build();

        // use the client to send the request
        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), clazz);
    }

    /**
     * This will do a post on the passed in API. It will then cast the results to
     * the passed in object.
     * 
     * @return The status code of the request.
     * @throws InterruptedException
     * @throws IOException
     */
    public <T> int post(String api, Map<String, Object> body) throws Exception {
        // create a request
        var request = getBuilder(api).POST(paramFormatter(body)).build();

        // use the client to send the request
        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        return response.statusCode();
    }

    /**
     * This will do a post on the passed in API. It will then cast the results to
     * the passed in object.
     * 
     * @return The passed in object class.
     * @throws InterruptedException
     * @throws IOException
     */
    public <T> T post(String api, Map<String, Object> body, Class<T> clazz) throws Exception {
        // create a request
        var request = getBuilder(api).POST(paramFormatter(body)).build();

        // use the client to send the request
        HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), clazz);
    }

    /**
     * Get the default base builder object for making a request to the API;
     * 
     * @param api The api to be hit.
     * @return The builder instance.
     */
    public Builder getBuilder(String api) {
        Builder httpBuilder = HttpRequest.newBuilder(URI.create(BASE_URL + api))
                .header("Content-Type", "application/json").header("Content-Type", "application/json");
        if (AUTH != null && !"".equals(this.AUTH.trim())) {
            httpBuilder.header("Authorization", this.AUTH);
        }
        return httpBuilder;
    }

    /**
     * Format params into a body publisher.
     * 
     * @param data The body to be formatted
     * @return {@link BodyPublisher}
     * @throws JsonProcessingException
     */
    private BodyPublisher paramFormatter(Map<String, Object> data) throws JsonProcessingException {
        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(data);
        return BodyPublishers.ofString(requestBody);
    }
}
