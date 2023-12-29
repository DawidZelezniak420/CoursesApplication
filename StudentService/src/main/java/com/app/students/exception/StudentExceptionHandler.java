package com.app.students.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StudentExceptionHandler {

    @ExceptionHandler(StudentException.class)
    public ResponseEntity<ErrorInfo> studentExceptionHandler(StudentException exception) {
        HttpStatus httpStatus;
        switch (exception.getStudentError()) {
            case STUDENT_NOT_FOUND -> httpStatus = HttpStatus.NOT_FOUND;
            case STUDENT_ALREADY_EXISTS -> httpStatus = HttpStatus.CONFLICT;
            default -> httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(new ErrorInfo(exception.getStudentError().getErrorMessage()));
    }
}
