package com.benodeveloper.medialibrary.handlers;

import com.benodeveloper.medialibrary.exceptions.ResourceException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class ResourceExceptionHandler {
    @ExceptionHandler(value = ResourceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ProblemDetail handle(ResourceException exception, HttpServletRequest request) {
         ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setDetail("Invalid Request");
        detail.setType(URI.create(request.getRequestURI()));

        detail.setProperty("error", exception.getMessage());
        return detail;
    }
}
