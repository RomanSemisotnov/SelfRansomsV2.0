package ru.marketboost.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        System.out.println("Java version is: " + System.getProperty("java.version"));
        SpringApplication.run(App.class, args);
    }

}
