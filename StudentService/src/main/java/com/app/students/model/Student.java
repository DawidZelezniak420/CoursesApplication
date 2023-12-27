package com.app.students.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@SequenceGenerator(name = "studentIdGenerator", initialValue = 10000, allocationSize = 1)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentIdGenerator")
    private Long studentId;
    @NotBlank(message = "First name cannot be null or blank")
    @Size(min = 3, message = "First name length cannot be less than 3")
    private String firstName;
    @NotBlank(message = "Last name cannot be null or blank")
    @Size(min = 3, message = "Last name length cannot be less than 3")
    private String lastName;
    @Column(unique = true)
    @NotBlank(message = "E-mail cannot be null or blank")
    @Email(message = "Invalid email format")
    private String email;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status cannot be null")
    private Status status;
    @JsonFormat(pattern = "yyyy-MM-dd 'T' HH:mm:ss")
    private LocalDateTime enrollDate;

    public Student() {
        enrollDate = LocalDateTime.now();
        status=Status.ACTIVE;
    }

    public enum Status {
        ACTIVE, INACTIVE
    }
}
