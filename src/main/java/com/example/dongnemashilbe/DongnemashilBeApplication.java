package com.example.dongnemashilbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DongnemashilBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DongnemashilBeApplication.class, args);
    }

}
