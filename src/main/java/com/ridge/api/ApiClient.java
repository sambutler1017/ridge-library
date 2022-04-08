package com.ridge.api;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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

    public ApiClient() {
        this.BASE_URL = "";
        this.objectMapper = new ObjectMapper();
    }

    public ApiClient(String url) {
        this.BASE_URL = url;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * This will do a get on the passed in API. It will then cast the results to the
     * passed in object.
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    public <T> T get(String api, Class<T> clazz) throws Exception {
        var client = HttpClient.newHttpClient();

        // create a request
        var request = getBuilder(api).GET().build();

        // use the client to send the request
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), clazz);
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
        var client = HttpClient.newHttpClient();

        // create a request
        var request = getBuilder(api).POST(paramFormatter(body)).build();

        // use the client to send the request
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
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
        var client = HttpClient.newHttpClient();

        // create a request
        var request = getBuilder(api).POST(paramFormatter(body)).build();

        // use the client to send the request
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        return response.statusCode();
    }

    /**
     * Get the default base builder object for making a request to the API;
     * 
     * @param api The api to be hit.
     * @return The builder instance.
     */
    public Builder getBuilder(String api) {
        return HttpRequest.newBuilder(URI.create(BASE_URL + api)).header("accept", "application/json");
    }

    /**
     * Format params into a body publisher.
     * 
     * @param data The body to be formatted
     * @return {@link BodyPublisher}
     */
    private BodyPublisher paramFormatter(Map<String, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return BodyPublishers.ofString(builder.toString());
    }
}
