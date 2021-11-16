package ru.marketboost.phone;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;

@SpringBootApplication(
        scanBasePackages = "ru.marketboost.phone",
        exclude = {DataSourceAutoConfiguration.class, LiquibaseAutoConfiguration.class}
)
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
