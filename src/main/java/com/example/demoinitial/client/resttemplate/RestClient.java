package com.example.demoinitial.client.resttemplate;

import com.example.demoinitial.domain.Person;
import com.example.demoinitial.utils.HasLogger;
import org.springframework.web.client.RestTemplate;

/**
 * Backend must be running
 */
public class RestClient implements HasLogger {
    public static void main(String[] args) {
        new RestClient().start();
    }
    public void start() {
        final String uri = "http://localhost:8080/api/persons/1";
        RestTemplate restTemplate = new RestTemplate();
        Person result = restTemplate.getForObject(uri, Person.class);
        getLogger().info(result.toString());
    }
}
