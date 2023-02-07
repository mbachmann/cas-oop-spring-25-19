package com.example.demoinitial;

import com.example.demoinitial.config.MyComponent;
import com.example.demoinitial.utils.HasLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;

@Controller
@SpringBootApplication
public class DemoInitialApplication implements HasLogger {

    @Autowired
    MyComponent myComponent;

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(DemoInitialApplication.class, args);
    }


    @PostConstruct
    public void afterInit() {
        boolean hasDevProfile = Arrays.asList(env.getActiveProfiles()).contains("dev");
        getLogger().info("Active Profiles: " + Arrays.toString(env.getActiveProfiles()) + "\n\n");
    }

}
