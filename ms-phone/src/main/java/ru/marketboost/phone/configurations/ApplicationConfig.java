package ru.marketboost.phone.configurations;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories("ru.marketboost.phone.hibernate.repositories")
@EnableTransactionManagement
@EnableJpaAuditing
class ApplicationConfig {

    @Bean
    public DataSource datasource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://45.141.102.46:5432/ransom-client-db")
                .username("ransomphonepguser922")
                .password("ransomphonepasspg432")
                .build();
    }

}