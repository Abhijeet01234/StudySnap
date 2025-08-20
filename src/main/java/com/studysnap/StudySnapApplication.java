package com.studysnap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.studysnap.model")
public class StudySnapApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudySnapApplication.class, args);
    }
}
