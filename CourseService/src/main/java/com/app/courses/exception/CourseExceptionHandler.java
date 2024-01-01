package com.app.courses.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CourseExceptionHandler {

    @ExceptionHandler(CourseException.class)
    public ResponseEntity<ErrorInfo> courseExceptionHandler(CourseException exception) {
        HttpStatus httpStatus;
        switch (exception.getCourseError()) {
            case COURSE_NOT_FOUND -> httpStatus = HttpStatus.NOT_FOUND;
            case COURSE_ALREADY_EXISTS
                    , COURSE_START_DATE_IS_AFTER_END_DATE
                    , COURSE_PARTICIPANTS_LIMIT_EXCEEDED
                    , COURSE_STATUS_CAN_NOT_BE_ACTIVE
                    , COURSE_STATUS_CAN_NOT_BE_FULL
                    , STUDENT_ALREADY_ENROLLED -> httpStatus = HttpStatus.CONFLICT;
            case COURSE_STATUS_IS_INACTIVE
                    , STUDENT_ID_DOES_NOT_EXISTS
                    , STUDENT_STATUS_IS_INACTIVE
                    , COURSE_STATUS_IS_FULL -> httpStatus = HttpStatus.BAD_REQUEST;
            case STUDENT_SERVICE_IS_UNAVAILABLE -> httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
            default -> httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(new ErrorInfo(exception.getCourseError().getErrorMessage()));
    }
}
