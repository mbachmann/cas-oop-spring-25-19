package com.example.demoinitial.web.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class SharedMockMvcTest {
    protected MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    protected String token;
    @Value("${app.jwtCookieName}")
    protected String jwtCookieName;

    protected Cookie cookie;


    protected void setUp()  {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected void setUpWithSecurity()  {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    protected void login (String username, String password) throws JsonProcessingException {

        String json = getLoginParamsJson(username, password);
        String uri = "/api/auth/signin";

        MvcResult postMvcResult = null;
        try {
            postMvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(json))
                .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (postMvcResult != null) {
            int status = postMvcResult.getResponse().getStatus();
            assertEquals(200, status);

            String[] tokens = getJwtFromCookies(postMvcResult.getResponse()).substring(jwtCookieName.length() + 1).split(";");
            token = tokens[0];
            cookie = new Cookie(jwtCookieName, token);
        }
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    protected <T> T mapFromJson(String json, Class<T> clazz)
        throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(json, clazz);
    }
    private String getLoginParamsJson(String username, String password) throws JsonProcessingException {
        return "{\"username\": " + "\"" + username + "\"" + ", \"password\": " + "\"" + password + "\"" + "}";
    }

    private String getJwtFromCookies(MockHttpServletResponse response) {
        List<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
        assertEquals(1, headers.size());
        return headers.get(0);
    }


}