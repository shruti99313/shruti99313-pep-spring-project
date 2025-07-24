package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserRegistrationTest {
    ApplicationContext app;
    HttpClient webClient;
    ObjectMapper objectMapper;

    private static final String BASE_URL = "http://localhost:8081"; // <-- updated to match your app

    @BeforeEach
    public void setUp() throws InterruptedException {
        webClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
        String[] args = new String[] {};
        app = SpringApplication.run(SocialMediaApp.class, args);
        Thread.sleep(500);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        Thread.sleep(500);
        SpringApplication.exit(app);
    }

    /**
     * Sending an http request to POST localhost:8081/register when username does not exist in the system
     */
    @Test
    public void registerUserSuccessful() throws IOException, InterruptedException {
        String json = "{\"username\":\"user\",\"password\":\"password\"}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/register")) // <-- updated port
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(200, status, "Expected Status Code 200 - Actual Code was: " + status);
    }

    /**
     * Sending an http request to POST localhost:8081/register when username already exists in system
     */
    @Test
    public void registerUserDuplicateUsername() throws IOException, InterruptedException {
        String json = "{\"username\":\"user\",\"password\":\"password\"}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/register")) // <-- updated port
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response1 = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response2 = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

        int status1 = response1.statusCode();
        int status2 = response2.statusCode();

        Assertions.assertEquals(200, status1, "Expected Status Code 200 - Actual Code was: " + status1);
        Assertions.assertEquals(409, status2, "Expected Status Code 409 - Actual Code was: " + status2);
    }
}
