package com.app.courses.service.notifications;

import com.app.courses.model.course.Course;
import com.app.courses.model.course.CourseMember;
import com.app.courses.model.dto.Notification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationCreator {

    public Notification createNotification(Course course) {
        return Notification.builder()
                .emails(getEmails(course))
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .description(course.getDescription())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .build();
    }

    private static List<String> getEmails(Course course) {
        return course.getCourseMembers().stream()
                .map(CourseMember::getEmail)
                .collect(Collectors.toList());
    }
}
