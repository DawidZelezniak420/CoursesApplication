package com.app.courses.validation;

import com.app.courses.exception.CourseError;
import com.app.courses.exception.CourseException;
import com.app.courses.model.course.Course;
import com.app.courses.model.course.Course.Status;
import com.app.courses.model.dto.StudentDto;
import com.app.courses.repository.CourseRepository;

public final class CourseValidator {

    public static void validateCourse(Course course) {
        validateCourseDate(course);
        validateParticipantsNumber(course);
        validateCourseStatus(course);
        validateCourseIsActive(course);
    }

    public static void validateCourseStatus(Course course) {
        if (Status.ACTIVE.equals(course.getStatus()) &&
                course.getParticipantsNumber().equals(course.getParticipantsLimit())) {
            throw new CourseException(CourseError.COURSE_STATUS_CAN_NOT_BE_ACTIVE);
        }
        if (Status.FULL.equals(course.getStatus()) &&
                !course.getParticipantsNumber().equals(course.getParticipantsLimit())) {
            throw new CourseException(CourseError.COURSE_STATUS_CAN_NOT_BE_FULL);
        }
    }

    public static void validateCourseExists(String courseId, CourseRepository courseRepository) {
        if (courseRepository.existsByCourseId(courseId)) {
            throw new CourseException(CourseError.COURSE_ALREADY_EXISTS);
        }
    }

    // check start date is after end date
    public static void validateCourseDate(Course course) {
        if (course.getStartDate().isAfter(course.getEndDate())) {
            throw new CourseException(CourseError.COURSE_START_DATE_IS_AFTER_END_DATE);
        }
    }

    public static void validateParticipantsNumber(Course course) {
        if (course.getParticipantsNumber() > course.getParticipantsLimit()) {
            throw new CourseException(CourseError.COURSE_PARTICIPANTS_LIMIT_EXCEEDED);
        } else if (course.getParticipantsNumber().equals(course.getParticipantsLimit())) {
            course.setStatus(Status.FULL);
        }
    }

    public static void validateStudentCanEnroll(Course course, StudentDto studentToEnroll) {
        if (StudentDto.Status.INACTIVE.equals(studentToEnroll.getStatus())) {
            throw new CourseException(CourseError.STUDENT_STATUS_IS_INACTIVE);
        }
        //check if such student is enrolled
        if (course.getCourseMembers().stream()
                .anyMatch(courseMember -> (courseMember.getEmail()).equals(studentToEnroll.getEmail()))) {
            throw new CourseException(CourseError.STUDENT_ALREADY_ENROLLED);
        }
    }

    public static void validateCourseStatusIsFull(Course course) {
        if (Status.FULL.equals(course.getStatus())) {
            throw new CourseException(CourseError.COURSE_STATUS_IS_FULL);
        }
    }

    public static void validateCourseIsActive(Course course) {
        if (Status.INACTIVE.equals(course.getStatus())) {
            throw new CourseException(CourseError.COURSE_STATUS_IS_INACTIVE);
        }
    }
}
