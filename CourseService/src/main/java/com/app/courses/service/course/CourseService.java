package com.app.courses.service.course;

import com.app.courses.model.course.Course;
import com.app.courses.model.dto.StudentDto;

import java.util.List;

public interface CourseService {

    Course addCourse(Course course);

    List<Course> getCoursesByStatus(Course.Status status);

    Course getCourseById(String courseId);

    void deleteCourse(String courseId);

    Course patchCourse(String courseId, Course newCourse);

    void enrollStudent(Long studentId, String courseId);

    void deleteEnrolledMember(String studentEmail, String courseId);

    void finishEnroll(String courseId);

    List<StudentDto> getEnrolledStudents(String courseId);

    List<Course> getAllCourses();

    void addListCourses(List<Course> courses);
}
