package ru.marketboost.library.common.http;

import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.marketboost.library.common.exceptions.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Slf4j
@ControllerAdvice
public class BaseHttpExceptionHandler {

    @Value("${spring.application.name}")
    protected String applicationName;

    @ExceptionHandler(MsModelNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleMsObjectNotFoundException(MsModelNotFoundException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MsAlreadyExistsException.class)
    public final ResponseEntity<ErrorResponse> handleMsAlreadyExistsException(MsAlreadyExistsException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MsBadRequestException.class)
    public final ResponseEntity<ErrorResponse> handleMsBadRequestException(MsBadRequestException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MsNotAuthorizedException.class)
    public final ResponseEntity<ErrorResponse> handleMsNotAuthorizedException(MsNotAuthorizedException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MsBadLoginPasswordException.class)
    public final ResponseEntity<ErrorResponse> handleMsBadLoginPasswordException(MsBadLoginPasswordException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MsNotAllowedException.class)
    public final ResponseEntity<ErrorResponse> handleMsNotAllowedException(MsNotAllowedException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MsInternalErrorException.class)
    public final ResponseEntity<ErrorResponse> handleMsInternalErrorException(MsInternalErrorException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        StringBuilder fields = new StringBuilder();
        for (FieldError field : fieldErrors) {
            fields.append(field.getField()).append(", ");
        }
        fields.delete(fields.length() - 2, fields.length());
        return handleMicroserviceException(new MsBadRequestException("MethodArgumentNotValidException: " + fields.toString()), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return handleMicroserviceException(new MsInternalErrorException(ex.getMessage()), request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    private ResponseEntity<ErrorResponse> handleMicroserviceException(MicroServiceException ex, WebRequest request, HttpStatus httpStatus) {
        HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();

        String requestBody = getRequestBody(httpRequest);
        String requestURL = getRequestURL(httpRequest);

        String errorMessage = String.format("SessionId: %s. appname: %s. exception: %s. url: %s. body: %s",
                getInternalSessionId(),
                applicationName,
                ex.getMessage(),
                requestURL,
                requestBody);
        //пишем детальную информацию в другой лог
        log.error("exception was thrown", ex);
        Sentry.captureException(new MicroServiceException(errorMessage, ex));

        //возвращаем пользователю ничего не значащую информацию об ошибке
        return new ResponseEntity<>(ErrorResponse.builder()
                .className(ex.getClass().getSimpleName())
                .code(String.valueOf(httpStatus.value()))
                .session(getInternalSessionId())
                .error(ex.getMessage())
                .build(), httpStatus);
    }

    private String getInternalSessionId() { //тут должен быть код сессии
        return UUID.randomUUID().toString();
    }

    private String getRequestURL(HttpServletRequest httpRequest) {
        return httpRequest.getRequestURL().toString();
    }

    private String getRequestBody(HttpServletRequest httpRequest) {
        String body = null;
        if (httpRequest.getMethod().equalsIgnoreCase("POST") || httpRequest.getMethod().equalsIgnoreCase("PUT")) {
            Scanner s;
            try {
                s = new Scanner(httpRequest.getInputStream(), StandardCharsets.UTF_8).useDelimiter("\\A");
            } catch (IOException ex) {
                return "Cant parse http body cause " + ex.getMessage();
            }
            body = s.hasNext() ? s.next() : "";
        }
        return body;
    }

}
