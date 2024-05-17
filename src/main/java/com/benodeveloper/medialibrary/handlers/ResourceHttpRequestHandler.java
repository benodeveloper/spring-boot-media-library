package com.benodeveloper.medialibrary.handlers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;

@RestControllerAdvice
public class ResourceHttpRequestHandler {
    @ExceptionHandler(value = NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ProblemDetail handleNoResourceFoundException(NoResourceFoundException exception, HttpServletRequest request) {
         ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        detail.setDetail("Invalid Request");
        detail.setType(URI.create(request.getRequestURI()));

        detail.setProperty("error", exception.getMessage());
        return detail;
    }

    @ExceptionHandler(value = MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ProblemDetail handleMultipartException(MultipartException exception, HttpServletRequest request) {
         ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setDetail("Invalid Request");
        detail.setType(URI.create(request.getRequestURI()));

        detail.setProperty("error", exception.getMessage());
        return detail;
    }

    @ExceptionHandler(value = MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ProblemDetail handleMissingServletRequestPartException(MissingServletRequestPartException exception, HttpServletRequest request) {
          ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setDetail("Invalid Request");
        detail.setType(URI.create(request.getRequestURI()));

        detail.setProperty("error", exception.getMessage());
        return detail;
    }
}
