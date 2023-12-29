package com.app.courses.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Notification {

    private List<String> emails;
    private String courseId;
    private String courseName;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
