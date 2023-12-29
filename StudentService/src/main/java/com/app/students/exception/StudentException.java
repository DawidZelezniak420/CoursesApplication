package com.app.students.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentException extends RuntimeException{
    private final StudentError studentError;
}
