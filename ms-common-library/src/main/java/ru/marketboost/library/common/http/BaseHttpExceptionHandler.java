package ru.marketboost.library.common.http;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.marketboost.library.common.exceptions.MsAlreadyExistsException;
import ru.marketboost.library.common.exceptions.MsBadRequestException;
import ru.marketboost.library.common.exceptions.MsNotAuthorizedException;

import java.util.List;

@ControllerAdvice
public class BaseHttpExceptionHandler {

    @ExceptionHandler(MsObjectNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleMsObjectNotFoundException(MsObjectNotFoundException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MsAlreadyExistsException.class)
    public final ResponseEntity<ErrorResponse> handleMsAlreadyExistsException(MsAlreadyExistsException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MsNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleMsObjectNotFoundException(MsNotFoundException ex, WebRequest request) {
        return handleMicroserviceException(ex, request, HttpStatus.NOT_FOUND);
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
        StringBuffer fields = new StringBuffer();
        for (FieldError field : fieldErrors) {
            fields.append(field.getField() + ", ");
        }
        fields.delete(fields.length() - 2, fields.length());
        return handleMicroserviceException(new MsBadRequestException(fields.toString()), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
        return handleMicroserviceException(new MsInternalErrorException(ex.getMessage()), request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
