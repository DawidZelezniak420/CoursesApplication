package com.app.courses.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDto {

    @NotNull(message = "Student ID cannot be null")
    private Long studentId;
    private String email;
    private Status status;

    public enum Status {
        ACTIVE, INACTIVE
    }
}
