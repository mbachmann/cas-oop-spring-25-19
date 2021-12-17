package com.example.demoinitial.client.resttemplate;

import com.example.demoinitial.domain.Person;
import com.example.demoinitial.utils.HasLogger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;



/**
 * Backend must be running
 */
public class RestClientBasicAuth implements HasLogger {
    public static void main(String[] args) {
        System.out.println("Basic Auth is not supported -> use JWT Auth");
        //new RestClientBasicAuth().start();
    }
    public void start() {
        final String uri = "http://localhost:8080/api/persons/1";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(createHeaders("admin@example.com", "admin"));
        ResponseEntity<Person> response = restTemplate.exchange(uri, HttpMethod.GET, request, Person.class);
        Person person = response.getBody();
        getLogger().info(person.toString());
    }

    HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
}
