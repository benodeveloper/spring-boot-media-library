package com.benodeveloper.medialibrary.handlers;

import com.benodeveloper.medialibrary.exceptions.FileOperationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class FileOperationHandler {
    @ExceptionHandler(value = FileOperationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody ProblemDetail handleFileOperationException(FileOperationException exception, HttpServletRequest request) {
         ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        detail.setDetail("Invalid Request");
        detail.setType(URI.create(request.getRequestURI()));

        detail.setProperty("error", exception.getMessage());
        return detail;
    }
}
