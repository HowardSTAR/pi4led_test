package com.sevagrigorev.pi4led_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Pi4ledTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(Pi4ledTestApplication.class, args);
    }
}
