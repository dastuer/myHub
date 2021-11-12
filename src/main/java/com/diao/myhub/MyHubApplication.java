package com.diao.myhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author Morty
 */
@SpringBootApplication
@EnableOpenApi
@EnableScheduling
public class MyHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyHubApplication.class, args);
    }
}
