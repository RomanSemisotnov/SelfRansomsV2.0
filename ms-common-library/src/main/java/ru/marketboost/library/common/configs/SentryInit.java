package ru.marketboost.library.common.configs;

import io.sentry.Sentry;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class SentryInit {

    @PostConstruct
    public void init() {
        Sentry.init("http://8e431e39f40047b8952f8f44eb88f5de@o1070561.ingest.sentry.io/6066598");
    }

}
