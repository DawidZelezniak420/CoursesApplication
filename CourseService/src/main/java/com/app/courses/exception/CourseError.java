package com.app.courses.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CourseError {
    COURSE_NOT_FOUND("Course not found!"),
    COURSE_ALREADY_EXISTS("Course already exists!"),
    COURSE_STATUS_DOES_NOT_EXISTS("Such course status does not exists!"),
    COURSE_START_DATE_IS_AFTER_END_DATE("Course start date is after end date!"),
    COURSE_PARTICIPANTS_LIMIT_EXCEEDED("The limit of course participants has been exceeded!"),
    COURSE_STATUS_CAN_NOT_BE_ACTIVE("Course status can not be ACTIVE, the participants limit has been reached!"),
    COURSE_STATUS_CAN_NOT_BE_FULL("Course status can not be FULL, the participants limit has not been reached!"),
    COURSE_STATUS_IS_INACTIVE("Course status is INACTIVE!"),
    STUDENT_STATUS_IS_INACTIVE("Student status is INACTIVE!"),
    STUDENT_ALREADY_ENROLLED("The student is already enrolled in this course!"),
    COURSE_STATUS_IS_FULL("You cannot add new participant because course is FULL!"),
    COURSE_MEMBERS_LIST_IS_EMPTY("There is not any enrolled student in this course!"),
    STUDENT_ID_DOES_NOT_EXISTS("Student with this ID does not exists!"),
    COURSE_MEMBER_EMAIL_IS_NULL("Course member email is null! Correct your data and try again.");
    private final String errorMessage;
}
