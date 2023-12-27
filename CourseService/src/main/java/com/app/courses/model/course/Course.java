package com.app.courses.model.course;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
public class Course {

    @Id
    @NotBlank(message = "Course ID cannot be blank")
    private String courseId;

    @NotBlank(message = "Course name cannot be blank")
    private String courseName;

    @NotBlank(message = "Course description cannot be blank")
    private String description;

    @NotNull(message = "Course start date cannot be null")
    @Future(message = "Course start date must be future date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @NotNull(message = "Course end date cannot be null")
    @Future(message = "Course end date must be future date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @NotNull(message = "Participants limit cannot be null")
    @Min(value = 1, message = "Participants limit cannot be lower than 1")
    private Long participantsLimit;

    private Long participantsNumber = START_VALUE;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Course status cannot be null")
    private Status status;

    List<CourseMember> courseMembers = new ArrayList<>();

    public void increaseParticipantsNumber() {
        ++participantsNumber;
        if (participantsLimit.equals(participantsNumber)) {
            setStatus(Status.FULL);
        }
    }

    public void decreaseParticipantsNumber() {
        --participantsNumber;
        if (!participantsLimit.equals(participantsNumber)) {
            setStatus(Status.ACTIVE);
        }
    }

    private static final Long START_VALUE = 0L;

    public Course() {
        status = Status.ACTIVE;
    }

    public enum Status {
        ACTIVE, INACTIVE, FULL
    }
}
