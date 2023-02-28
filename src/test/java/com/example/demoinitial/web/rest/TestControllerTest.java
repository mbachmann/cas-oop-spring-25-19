package com.example.demoinitial.web.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class TestControllerTest extends AbstractMockMvcTest{

    String baseUri = "/api/test";

    @Override
    @BeforeEach
    public void setUp()  {
        super.setUpWithSecurity();
    }

    @Test
    public void souldGetPublicContent() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(baseUri + "/all")
            .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String response = mvcResult.getResponse().getContentAsString();
        assertEquals("Public Content.", response);
    }

    @Test
    public void souldGet401ForUserContent() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(baseUri + "/user")
            .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(401, status);
    }

    @Test
    public void souldGetUserContent() throws Exception {
        super.login("user", "user");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(baseUri + "/user")
            .cookie(cookie)
            .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String response = mvcResult.getResponse().getContentAsString();
        assertEquals("User Content.", response);
    }

    @Test
    public void souldGet403ForAdminContent() throws Exception {
        super.login("user", "user");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(baseUri + "/admin")
            .cookie(cookie)
            .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(403, status);
    }

    @Test
    public void souldGetAdminContent() throws Exception {
        super.login("admin", "admin");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(baseUri + "/admin")
            .cookie(cookie)
            .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String response = mvcResult.getResponse().getContentAsString();
        assertEquals("Admin Board.", response);
    }


}
