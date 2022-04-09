package com.ridge.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ridge.api.ApiClient;
import com.ridge.test.domain.AuthToken;
import com.ridge.test.domain.User;

/**
 * Example test class for testing the api client library
 * 
 * @author Sam Butler
 * @since April 8, 2022
 */
public class ApiClientTest {
    public static void run() throws Exception {
        ApiClient client = new ApiClient("https://marcs-microservice.herokuapp.com");
        Map<String, Object> auth = new HashMap<>();
        auth.put("email", "sambutler1017@icloud.com");
        auth.put("password", "78e05bf74fad284798a195ec2ff3ae6D!");

        AuthToken data = client.post("/authenticate", auth, AuthToken.class);
        client.setAuthorization("Bearer: " + data.getToken());

        List<User> testList = Arrays.asList(client.get("/api/user-app/user-profile", User[].class));
        int test = 1;
    }
}
