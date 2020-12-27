package com.eleren.jobofferorganiser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class JobOfferOrganiserApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(JobOfferOrganiserApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(JobOfferOrganiserApplication.class, args);
    }

}
