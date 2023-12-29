package com.app.students.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StudentError {
    STUDENT_NOT_FOUND("Student not found!"),
    STUDENT_ALREADY_EXISTS("Student with this e-mail already exists!"),
    STUDENT_STATUS_IS_INACTIVE("Student status is INACTIVE!");

    private final String errorMessage;
}
