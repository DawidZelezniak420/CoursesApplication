package com.app.courses.service.notifications;

import com.app.courses.model.dto.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class EmailSenderImpl implements EmailSender {

    private static final String SENDING_NOTIFICATION_ERROR = "Notification has not been sent!";

    private final JavaMailSender mailSender;

    @Autowired
    public EmailSenderImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendCourseReminders(Notification notification) {
        String title = "Remember about you course: " + notification.getCourseName();
        String content = buildEmailContent(notification).toString();
        notification.getEmails().forEach(studentEmail -> {
            try {
                buildAndSendEmail(studentEmail, title, content);
            } catch (MessagingException e) {
                log.error(SENDING_NOTIFICATION_ERROR);
            }
        });
    }

    @Async
    void buildAndSendEmail(String to, String title, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setText(content, false);
        mailSender.send(mimeMessage);
    }


    private StringBuilder buildEmailContent(Notification notification) {
        StringBuilder content = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");
        content.append("Course ").append(notification.getCourseName());
        content.append(" starts at ").append(notification.getStartDate().format(dateFormatter));
        content.append(". Please log in 15 minutes before the course starts.");
        content.append("\n");
        content.append("Course description: ").append(notification.getDescription());
        content.append("\n");
        content.append("End of the course: ").append(notification.getEndDate().format(dateFormatter));
        content.append("\n");
        content.append("Course id: ").append(notification.getCourseId()).append("\n");
        content.append("We are waiting for you :)");
        return content;
    }
}
