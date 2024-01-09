package com.app.courses.service.course;

import com.app.courses.exception.CourseError;
import com.app.courses.exception.CourseException;
import com.app.courses.model.course.Course;
import com.app.courses.model.course.CourseMember;
import com.app.courses.model.dto.Notification;
import com.app.courses.model.dto.StudentDto;
import com.app.courses.repository.CourseRepository;
import com.app.courses.service.notifications.EmailSender;
import com.app.courses.service.notifications.NotificationCreator;
import com.app.courses.validation.CourseValidator;
import feign.FeignException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private final StudentFeignClient feignClient;

    private final NotificationCreator notificationCreator;

    private final EmailSender emailSender;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, StudentFeignClient feignClient
            , NotificationCreator notificationCreator, EmailSender emailSender) {
        this.courseRepository = courseRepository;
        this.feignClient = feignClient;
        this.notificationCreator = notificationCreator;
        this.emailSender = emailSender;
    }

    // method for quickly adding courses
    public void addListCourses(List<Course> courses) {
        if (courses != null) {
            courseRepository.saveAll(courses);
        }
    }

    public Course addCourse(Course course) {
        CourseValidator.validateCourse(course);
        CourseValidator.validateCourseExists(course.getCourseId(), courseRepository);
        return courseRepository.save(course);
    }


    // get courses by status or get all if status is null
    public List<Course> getCoursesByStatus(Course.Status status) {
        return status != null ? courseRepository.findAllByStatus(status) : courseRepository.findAll();
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(String courseId) throws CourseException {
        return courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));
    }

    public void deleteCourse(String courseId) throws CourseException {
        Course courseFormDb = getCourseById(courseId);
        courseRepository.delete(courseFormDb);
    }

    // update course without course ID and participants number
    public Course patchCourse(String courseId, Course newCourse) {
        Course courseFromDb = getCourseById(courseId);
        setCourseObject(courseFromDb, newCourse);
        CourseValidator.validateCourse(courseFromDb);
        return courseRepository.save(courseFromDb);
    }

    public void enrollStudent(Long studentId, String courseId) {
        Course courseFromDb = getCourseById(courseId);
        CourseValidator.validateCourseIsActive(courseFromDb);
        CourseValidator.validateCourseStatusIsFull(courseFromDb);
        StudentDto studentToEnroll = sendRequestToStudentService(studentId);
        CourseValidator.validateStudentCanEnroll(courseFromDb, studentToEnroll);
        courseFromDb.getCourseMembers().add(new CourseMember(studentToEnroll.getEmail()));
        courseFromDb.increaseParticipantsNumber();
        courseRepository.save(courseFromDb);
    }

    public void deleteEnrolledMember(String memberEmail, String courseId) {
        Course course = getCourseById(courseId);
        CourseValidator.validateCourseIsActive(course);
        List<CourseMember> courseMembers = course.getCourseMembers();
        courseMembers.removeIf(courseMember -> courseMember.getEmail().equals(memberEmail));
        course.decreaseParticipantsNumber();
        courseRepository.save(course);
    }

    // throw exception if student service is unavailable
    // or student id does not exists in database
    private StudentDto sendRequestToStudentService(Long studentId) {
        StudentDto studentToEnroll = new StudentDto();
        try {
            studentToEnroll = feignClient.getStudentById(studentId);
        } catch (FeignException ex) {
            if (ex.status() == HttpStatus.SC_NOT_FOUND) {
                throw new CourseException(CourseError.STUDENT_ID_DOES_NOT_EXISTS);
            } else if (ex.status() == HttpStatus.SC_SERVICE_UNAVAILABLE) {
                throw new CourseException(CourseError.STUDENT_SERVICE_IS_UNAVAILABLE);
            } else throw new CourseException(CourseError.INTERNAL_SERVER_ERROR);
        }
        return studentToEnroll;
    }

    public List<StudentDto> getEnrolledStudents(String courseId) {
        Course courseFromDb = getCourseById(courseId);
        List<String> emails = courseFromDb.getCourseMembers()
                .stream().map(CourseMember::getEmail)
                .collect(Collectors.toList());
        return feignClient.getStudentsByEmails(emails);
    }

    public void finishEnroll(String courseId) {
        Course courseFromDb = getCourseById(courseId);
        CourseValidator.validateCourseIsActive(courseFromDb);
        courseFromDb.setStatus(Course.Status.INACTIVE);
        courseRepository.save(courseFromDb);
        sendNotification(courseFromDb);
    }

    private void sendNotification(Course courseFromDb) {
        Notification notification = notificationCreator.createNotification(courseFromDb);
        emailSender.sendCourseReminders(notification);
    }

    private void setCourseObject(Course courseFromDb, Course newCourse) {
        courseFromDb.setCourseName(newCourse.getCourseName());
        courseFromDb.setDescription(newCourse.getDescription());
        courseFromDb.setStartDate(newCourse.getStartDate());
        courseFromDb.setEndDate(newCourse.getEndDate());
        courseFromDb.setParticipantsLimit(newCourse.getParticipantsLimit());
        courseFromDb.setStatus(newCourse.getStatus());
    }
}
