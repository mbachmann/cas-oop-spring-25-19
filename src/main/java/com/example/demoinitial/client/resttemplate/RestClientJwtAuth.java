package com.example.demoinitial.client.resttemplate;

import com.example.demoinitial.domain.Person;
import com.example.demoinitial.utils.HasLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collections;


/**
 * Backend must be running
 */
public class RestClientJwtAuth implements HasLogger {

    private final String autenticateUri = "http://localhost:8080/api/authenticate";
    final String uri = "http://localhost:8080/api/persons/1";

    RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) throws JsonProcessingException {
        new RestClientJwtAuth().start();
    }
    public void start() throws JsonProcessingException {

        String token = getLoginToken("admin@example.com", "admin");

        HttpEntity<String> request = new HttpEntity<String>(createHeaders(token));
        ResponseEntity<Person> response = restTemplate.exchange(uri, HttpMethod.GET, request, Person.class);
        Person person = response.getBody();
        getLogger().info(person.toString());
    }


    private String getLoginToken(String username, String password) throws JsonProcessingException {

        String json = getLoginParamsJson(username, password);
        HttpEntity<String> request = new HttpEntity<String>(json, createHeaders());
        ResponseEntity<String> response = restTemplate.postForEntity(autenticateUri, request, String.class);
        return response.getHeaders().get("Authorization").get(0);
    }

    private String getLoginParamsJson(String username, String password) throws JsonProcessingException {
        return "{\"email\": " + "\"" + username + "\"" + ", \"password\": " + "\"" + password + "\"" + "}";
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private HttpHeaders createHeaders(String token) {

        HttpHeaders headers = createHeaders();
        headers.set( "Authorization", token );
        return headers;
    }
}
