package com.app.courses.exception;

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
            case COURSE_NOT_FOUND
                    , COURSE_MEMBERS_LIST_IS_EMPTY -> httpStatus = HttpStatus.NOT_FOUND;
            case COURSE_ALREADY_EXISTS
                    , COURSE_START_DATE_IS_AFTER_END_DATE
                    , COURSE_PARTICIPANTS_LIMIT_EXCEEDED
                    , COURSE_STATUS_CAN_NOT_BE_ACTIVE
                    , COURSE_STATUS_CAN_NOT_BE_FULL
                    , STUDENT_ALREADY_ENROLLED -> httpStatus = HttpStatus.CONFLICT;
            case COURSE_STATUS_DOES_NOT_EXISTS
                    , COURSE_STATUS_IS_INACTIVE
                    , STUDENT_STATUS_IS_INACTIVE
                    , COURSE_STATUS_IS_FULL
                    , COURSE_MEMBER_EMAIL_IS_NULL -> httpStatus = HttpStatus.BAD_REQUEST;
            default -> httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(new ErrorInfo(exception.getCourseError().getErrorMessage()));
    }
}
