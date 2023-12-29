package com.app.courses.model.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class CourseMember {

    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd 'T' HH:mm:ss")
    private LocalDateTime enrollDate;

    public CourseMember(String email) {
        this.email = email;
        enrollDate = LocalDateTime.now();
    }
}
