package ru.marketboost.library.common.http.services;

import com.google.gson.Gson;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ru.marketboost.library.common.exceptions.*;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;

@Slf4j
public abstract class BaseMicroService {

    private final String applicationGroup = "StartUP@";
    private final String serviceName;
    protected final RestTemplate restTemplate;

    @Value("${http.retry.count}")
    private final int retryN = 5;
    @Value("${http.retry.time}")
    private final int sleepTime = 1;

    private Map<String, Function<ErrorResponse, MicroServiceException>> exceptionFactory = new HashMap<>();

    protected BaseMicroService(String serviceName, RestTemplate restTemplate) {
        this.serviceName = serviceName;
        this.restTemplate = restTemplate;

        initializeExceptions();
    }

    protected String buildUrl(String path) {
        return String.format(
                "http://%s%s", serviceName, path
        );
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
                    log.error("trying to send http reqeust was failed", ex);
                    Sentry.captureException(ex);
                    Thread.sleep(sleepTime * 1000);
                }
            } catch (InterruptedException ex) {
                log.error("trying to make pause between http requests was failed", ex);
                Sentry.captureException(ex);
            }
        }
        throw new MsInternalErrorException("Timeout trying send http request");
    }

    private MicroServiceException createException(HttpStatusCodeException ex) {
        String body = ex.getResponseBodyAsString();
        ErrorResponse errorResponse = new Gson().fromJson(body, ErrorResponse.class);

        if (errorResponse == null || errorResponse.getClassName() == null
                || errorResponse.getClassName().isEmpty()
                || !exceptionFactory.containsKey(errorResponse.getClassName())) {
            return new MicroServiceException(body);
        }

        return exceptionFactory.get(errorResponse.getClassName()).apply(errorResponse);
    }

    private void initializeExceptions() {
        exceptionFactory.put(MsModelNotFoundException.class.getSimpleName(), error -> new MsModelNotFoundException(error.getError()));

        exceptionFactory.put(MsAlreadyExistsException.class.getSimpleName(), error -> new MsAlreadyExistsException(error.getError()));

        exceptionFactory.put(MsBadLoginPasswordException.class.getSimpleName(), error -> new MsBadLoginPasswordException());

        exceptionFactory.put(MsNotAllowedException.class.getSimpleName(), error -> new MsNotAllowedException());

        exceptionFactory.put(MsInternalErrorException.class.getSimpleName(), error -> new MsInternalErrorException(error.getError()));

        exceptionFactory.put(MsBadRequestException.class.getSimpleName(), error -> new MsBadRequestException(error.getError()));

        exceptionFactory.put(MsNotAuthorizedException.class.getSimpleName(), error -> new MsNotAuthorizedException());
    }

}
