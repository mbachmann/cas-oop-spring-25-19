package com.example.demoinitial.config;

import com.example.demoinitial.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyBean implements CommandLineRunner {

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
        System.out.println("getTestValue = " + myComponent.getTestValue());
        System.out.println("Felix Muster Test " + felixMuster.toString());
        System.out.println("Max Mustermann Test " + maxMustermann.toString());
    }
}
