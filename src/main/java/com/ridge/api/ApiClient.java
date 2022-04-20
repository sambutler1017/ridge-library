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

import rx.subjects.BehaviorSubject;

/**
 * Api client class for consuming restful endpoints.
 * 
 * @author Sam Butler
 * @since April 8, 2022
 */
public class ApiClient {
    private final String BASE_URL;

    private final ObjectMapper objectMapper;

    private final HttpClient httpClient;

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
     * @param api   The endpoint to hit.
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
     * @param api  The endpoint to hit.
     * @param body The body to pass with the post request.
     * @return The {@link BehaviorSubject} of the response.
     * @throws InterruptedException
     * @throws IOException
     */
    public HttpResponse<String> post(String api, Map<String, Object> body) throws Exception {
        var request = getBuilder(api).POST(paramFormatter(body)).build();
        return send(request);
    }

    /**
     * This will do a post on the passed in API. It will then cast the results to
     * the passed in object.
     * 
     * @param <T>   The object to cast the result as.
     * @param api   The endpoint to hit.
     * @param body  The body to pass with the post request.
     * @param clazz The class to cast it too.
     * @return The passed in object class.
     * @throws InterruptedException
     * @throws IOException
     */
    public <T> T post(String api, Map<String, Object> body, Class<T> clazz) throws Exception {
        var request = getBuilder(api).POST(paramFormatter(body)).build();
        return send(request, clazz);
    }

    /**
     * This will do a post on the passed in API. It will then cast the results to
     * the passed in object.
     * 
     * @param <T>  The object to cast the result as.
     * @param api  The endpoint to hit.
     * @param body The body to pass with the post request.
     * @return The passed in object class.
     * @throws InterruptedException
     * @throws IOException
     */
    public <T> HttpResponse<String> post(String api, T body) throws Exception {
        String jsonData = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(body);
        var request = getBuilder(api).POST(BodyPublishers.ofString(jsonData)).build();
        return send(request);
    }

    /**
     * This will do a post on the passed in API. It will then cast the results to
     * the passed in object.
     * 
     * @param <T>   The object to cast the result as.
     * @param api   The endpoint to hit.
     * @param body  The body to pass with the post request.
     * @param clazz The class to cast it too.
     * @return The passed in object class.
     * @throws InterruptedException
     * @throws IOException
     */
    public <T, R> R post(String api, T body, Class<R> clazz) throws Exception {
        String jsonData = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(body);
        var request = getBuilder(api).POST(BodyPublishers.ofString(jsonData)).build();
        return send(request, clazz);
    }

    /**
     * This will do a get on the passed in API. It will then cast the results to the
     * passed in object. It will wrap the data returned in a subject to watch when
     * the API returns.
     * 
     * @param api   The endpoint to hit.
     * @param clazz The class to cast it too.
     * @throws Exception
     */
    public <T> BehaviorSubject<T> getAsync(String api, Class<T> clazz) throws Exception {
        var request = getBuilder(api).GET().build();
        return sendAsync(request, clazz);
    }

    /**
     * This will do a post on the passed in API. It will then cast the results to
     * the passed in object. It will wrap the data returned in a subject to watch
     * when the API returns.
     * 
     * @param api  The endpoint to hit.
     * @param body The body to pass with the post request.
     * @return The {@link BehaviorSubject} of the response.
     * @throws InterruptedException
     * @throws IOException
     */
    public BehaviorSubject<HttpResponse<String>> postAsync(String api, Map<String, Object> body) throws Exception {
        var request = getBuilder(api).POST(paramFormatter(body)).build();
        return sendAsync(request);
    }

    /**
     * This will do a post on the passed in API. It will then cast the results to
     * the passed in object. It will wrap the data returned in a subject to watch
     * when the API returns.
     * 
     * @param <T>   The object to cast the result as.
     * @param api   The endpoint to hit.
     * @param body  The body to pass with the post request.
     * @param clazz The class to cast it too.
     * @return The passed in object class.
     * @throws InterruptedException
     * @throws IOException
     */
    public <T> BehaviorSubject<T> postAsync(String api, Map<String, Object> body, Class<T> clazz) throws Exception {
        var request = getBuilder(api).POST(paramFormatter(body)).build();
        return sendAsync(request, clazz);
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
    public HttpResponse<String> send(HttpRequest req) throws Exception {
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
     * @return The generic object of the data.
     * @throws Exception If the request could not be sent.
     */
    public <T> T send(HttpRequest req, Class<T> clazz) throws Exception {
        return objectMapper.readValue(send(req).body(), clazz);
    }

    /**
     * This will perform the api call to send the request. It will than consume the
     * api and get the response. It will return the {@link HttpResponse} object that
     * was returned in a subject to listen too.
     * 
     * @param req The request to send.
     * @return {@link HttpResponse} data object.
     * @throws Exception If the request could not be sent.
     */
    public BehaviorSubject<HttpResponse<String>> sendAsync(HttpRequest req) throws Exception {
        BehaviorSubject<HttpResponse<String>> subject = BehaviorSubject.create();
        httpClient.sendAsync(req, BodyHandlers.ofString()).whenComplete((response, error) -> subject.onNext(response));
        return subject;
    }

    /**
     * This will perform the api call to send the request. It will than consume the
     * api and get the response. Once the response is returned it will then cast the
     * response to the passed in class data type in a subject to listen too.
     * 
     * @param <T>   The object to cast the result as.
     * @param req   The request to send.
     * @param clazz The class object.
     * @return {@link BehaviorSubject} data object.
     * @throws Exception If the request could not be sent.
     */
    public <T> BehaviorSubject<T> sendAsync(HttpRequest req, Class<T> clazz) throws Exception {
        BehaviorSubject<T> subject = BehaviorSubject.create();
        httpClient.sendAsync(req, BodyHandlers.ofString()).whenComplete((response, error) -> {
            if (response != null) {
                try {
                    subject.onNext(objectMapper.readValue(response.body(), clazz));
                } catch (Exception e) {
                    subject.onNext(null);
                }
            } else {
                subject.onNext(null);
            }
        });
        return subject;
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
                .header("accept", "application/json");
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
