package com.app.courses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CoursesCvApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoursesCvApplication.class, args);
    }

}
