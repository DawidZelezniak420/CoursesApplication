package com.app.courses.service.notifications;

import com.app.courses.model.dto.Notification;

public interface EmailSender {

    void sendCourseReminders(Notification notificationDto);
}
