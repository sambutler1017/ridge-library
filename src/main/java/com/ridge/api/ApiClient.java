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
        var request = getBuilder(api).GET().build();
        return send(request, clazz);
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
        var request = getBuilder(api).POST(paramFormatter(body)).build();
        return send(request).statusCode();
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
        var request = getBuilder(api).POST(paramFormatter(body)).build();
        return send(request, clazz);
    }

    /**
     * This will perform the api call to send the request. It will than consume the
     * api and get the response. It will return the {@link HttpResponse} object that
     * was returned.
     * 
     * @param req The request to send.
     * @return {@link HttpResponse} data object.
     * @throws Exception If the request could not be sent.
     */
    private HttpResponse<String> send(HttpRequest req) throws Exception {
        return httpClient.send(req, BodyHandlers.ofString());
    }

    /**
     * This will perform the api call to send the request. It will than consume the
     * api and get the response. Once the response is returned it will then cast the
     * response to the passed in class data type.
     * 
     * @param <T>   The object to cast the result as.
     * @param req   The request to send.
     * @param clazz The class object.
     * @return {@link HttpResponse} data object.
     * @throws Exception If the request could not be sent.
     */
    private <T> T send(HttpRequest req, Class<T> clazz) throws Exception {
        var response = send(req);
        return objectMapper.readValue(response.body(), clazz);
    }

    /**
     * Get the default base builder object for making a request to the API;
     * 
     * @param api The api to be hit.
     * @return The builder instance.
     */
    private Builder getBuilder(String api) {
        Builder httpBuilder = HttpRequest
                .newBuilder(URI.create(BASE_URL + api))
                .header("Content-Type", "application/json")
                .header("Content-Type", "application/json");
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
