package com.example.demoinitial;

import com.example.demoinitial.config.MyComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class DemoInitialApplication {

    @Autowired
    MyComponent myComponent;

    public static void main(String[] args) {
        SpringApplication.run(DemoInitialApplication.class, args);
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World " + myComponent.getPerson().toString();
    }

}
