package com.app.courses.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CourseRuntimeError {

    COURSE_STATUS_IS_INACTIVE("Course status is INACTIVE!"),
    STUDENT_STATUS_IS_INACTIVE("Student status is INACTIVE!"),
    STUDENT_ALREADY_ENROLLED("The student is already enrolled in this course!"),
    COURSE_STATUS_IS_FULL("You cannot add new participant because course is FULL!"),
    STUDENT_ID_DOES_NOT_EXISTS("A student with such ID does not exists!"),
    STUDENT_SERVICE_IS_UNAVAILABLE("Student service is unavailable now, try again later.");
    private final String errorMessage;
}
