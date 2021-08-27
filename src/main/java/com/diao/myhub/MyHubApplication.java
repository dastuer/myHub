package com.diao.myhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
public class MyHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyHubApplication.class, args);
    }
}
