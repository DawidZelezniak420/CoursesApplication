package com.app.courses.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CourseException extends RuntimeException {
    private CourseError courseError;
}
