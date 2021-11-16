package ru.marketboost.library.common.http.services;

import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ru.marketboost.library.common.exceptions.MicroServiceException;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

public abstract class BaseMicroService {

    private final String serviceName;
    protected final RestTemplate restTemplate;

    private final int retryN = 5;
    private final int sleepTime = 1;

    protected BaseMicroService(String serviceName, RestTemplate restTemplate) {
        this.serviceName = serviceName;
        this.restTemplate = restTemplate;

        initializeExceptions();
    }

    protected <T> T retry(Callable<T> callable) throws MicroServiceException {
        int attempt = 0;
        while (attempt++ < retryN) {
            try {
                try {
                    return callable.call();
                } catch (ResourceAccessException | IllegalStateException ex) {
                    //если сервис к которому мы пытаемся обратиться не зарегистрирован в консуле
                    //будем в него долбиться пока не получится
                    if (ex.getCause() == null) {
                        log.error(ex.getMessage());
                    } else if (ex.getCause() instanceof UnknownHostException) {
                        log.error("Не удалось определить хост " + applicationGroup + serviceName);
                    } else if (ex.getCause() instanceof SocketException) {
                        log.error("Разрыв соединиения при выполнении запроса");
                    } else {
                        log.error(ex.getCause().getMessage());
                    }
                    attempt--;
                    Thread.sleep(sleepTime * 1000);
                } catch (HttpStatusCodeException ex) {
                    throw createException(ex);
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    if (log.isDebugEnabled()) {
                        log.error(ex.getMessage(), ex);
                    }
                    Thread.sleep(sleepTime * 1000);
                }
            } catch (InterruptedException ex) {
                log.error(ex.getMessage());
            }
        }
        throw new Inter("Timeout trying send http request");
    }


}
