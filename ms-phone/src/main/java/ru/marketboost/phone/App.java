package ru.marketboost.phone;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class App {

    public static void main(String[] args) {
        System.out.println("Java version is: " + System.getProperty("java.version"));
        SpringApplication.run(App.class, args);
    }

}
