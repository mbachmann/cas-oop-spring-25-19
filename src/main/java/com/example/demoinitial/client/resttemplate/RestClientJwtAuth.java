package com.example.demoinitial.client.resttemplate;

import com.example.demoinitial.domain.Person;
import com.example.demoinitial.utils.HasLogger;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import org.springframework.web.util.WebUtils;

/**
 * Backend must be running
 */
public class RestClientJwtAuth implements HasLogger {

    private final String autenticateUri = "http://localhost:8080/api/auth/signin";
    final String uri = "http://localhost:8080/api/persons/1";

    RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) throws JsonProcessingException {
        new RestClientJwtAuth().start();
    }
    public void start() throws JsonProcessingException {

        String token = getLoginToken("admin", "admin");

        HttpEntity<String> request = new HttpEntity<String>(createHeaders(token));
        ResponseEntity<Person> response = restTemplate.exchange(uri, HttpMethod.GET, request, Person.class);
        Person person = response.getBody();
        getLogger().info(person.toString());
    }


    private String getLoginToken(String username, String password) throws JsonProcessingException {

        String json = getLoginParamsJson(username, password);
        HttpEntity<String> request = new HttpEntity<String>(json, createHeaders());
        ResponseEntity<String> response = restTemplate.postForEntity(autenticateUri, request, String.class);
        return getJwtFromCookies(response);
    }

    private String getLoginParamsJson(String username, String password) throws JsonProcessingException {
        return "{\"username\": " + "\"" + username + "\"" + ", \"password\": " + "\"" + password + "\"" + "}";
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private HttpHeaders createHeaders(String token) {

        HttpHeaders headers = createHeaders();
        headers.set( "Cookie", token );
        return headers;
    }

    private String getJwtFromCookies(ResponseEntity<String> response) {
        HttpHeaders headers = response.getHeaders();
        return headers.getFirst(HttpHeaders.SET_COOKIE);

    }
}
