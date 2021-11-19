package ru.marketboost.phone.configurations;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Configuration
@EnableJpaRepositories("ru.marketboost.phone.hibernate.repositories")
@EnableTransactionManagement
@EnableJpaAuditing(dateTimeProviderRef = "mscDateTimeProvider")
class ApplicationConfig {

    @Bean
    public DataSource datasource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://45.142.36.57:5432/ransom-client-db?serverTimezone=Europe/Moscow")
                .username("ransomphonepguser922")
                .password("ransomphonepasspg432")
                .build();
    }

    @Bean
    public DateTimeProvider mscDateTimeProvider() {
        return () -> Optional.of(LocalDateTime.now(
                ZoneOffset.of("+03:00")
        ));
    }

}
