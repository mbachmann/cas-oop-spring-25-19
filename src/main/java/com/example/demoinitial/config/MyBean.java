package com.example.demoinitial.config;

import com.example.demoinitial.domain.Person;
import com.example.demoinitial.utils.HasLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyBean implements CommandLineRunner, HasLogger {

    MyComponent myComponent;

    @Autowired
    @Qualifier("felixMuster")
    Person felixMuster;

    @Autowired
    @Qualifier("maxMustermann")
    Person maxMustermann;

    @Autowired
    public MyBean(MyComponent myComponent) {
        this.myComponent = myComponent;
    }

    public void run(String[] args) {
        myComponent.hello();
        getLogger().info("getTestValue = " + myComponent.getTestValue());
        getLogger().debug("Felix Muster Test " + felixMuster.toString());
        getLogger().debug("Max Mustermann Test " + maxMustermann.toString());
    }
}
