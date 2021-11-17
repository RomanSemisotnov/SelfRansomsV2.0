package ru.marketboost.phone;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;

@SpringBootApplication(
        scanBasePackages = {
                "ru.marketboost.phone",
                "ru.marketboost.library.common.http",
                "ru.marketboost.library.common.configs"
        },
        exclude = {DataSourceAutoConfiguration.class, LiquibaseAutoConfiguration.class}
)
public class MsPhone {

    public static void main(String[] args) {
        SpringApplication.run(MsPhone.class, args);
    }

}
