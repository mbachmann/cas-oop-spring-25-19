package com.example.demoinitial.config;

import com.example.demoinitial.domain.Person;
import com.example.demoinitial.utils.HasLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MyComponent implements HasLogger {

    @Value("${com.example.test}")
    private String testValue;

    public void hello() {
        getLogger().info("Ich bin MyComponent");
    }

    @Bean(name="felixMuster")
    public Person getPerson() {
        Person person = new Person();
        person.setFirstName("Felix");
        person.setLastName("Muster");
        return person;
    }


    @Bean(name="maxMustermann")
    public Person getPersonMaxMustermann() {
        Person person = new Person();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        return person;
    }

    public String getTestValue() {
        return testValue;
    }
}
