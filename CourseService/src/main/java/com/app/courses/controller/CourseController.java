package com.app.courses.controller;

import com.app.courses.model.course.Course;
import com.app.courses.model.dto.StudentDto;
import com.app.courses.service.course.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody @Valid Course course) {
        return ResponseEntity.ok().body(courseService.addCourse(course));
    }

    // quick add list of courses(More info in TestingData class)
    @PostMapping("/add/list/")
    public void addCoursesList(@RequestBody List<Course> courses) {
        courseService.addListCourses(courses);
    }

    @GetMapping("/by/status/")
    public ResponseEntity<List<Course>> getCoursesByStatus(@RequestParam(required = false) Course.Status status) {
        return ResponseEntity.ok().body(courseService.getCoursesByStatus(status));
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok().body(courseService.getAllCourses());
    }

    @GetMapping("/{courseId}/")
    public ResponseEntity<Course> getCourseById(@PathVariable String courseId) {
        return ResponseEntity.ok().body(courseService.getCourseById(courseId));
    }

    @DeleteMapping("/{courseId}/")
    public ResponseEntity<?> deleteCourse(@PathVariable String courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok().build();
    }

    // update course except course id and participants number
    @PatchMapping("/{courseId}/")
    public ResponseEntity<Course> patchCourse(@PathVariable String courseId, @RequestBody @Valid Course newCourse) {
        return ResponseEntity.ok().body(courseService.patchCourse(courseId, newCourse));
    }

    @PostMapping("/enroll/students/{studentId}/courses/{courseId}/")
    public ResponseEntity<?> enrollStudent(@PathVariable Long studentId, @PathVariable String courseId) {
        courseService.enrollStudent(studentId, courseId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/finish/enroll/{courseId}/")
    public ResponseEntity<?> finishEnroll(@PathVariable String courseId) {
        courseService.finishEnroll(courseId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{courseId}/members/")
    public ResponseEntity<List<StudentDto>> getEnrolledStudents(@PathVariable String courseId) {
        return ResponseEntity.ok().body(courseService.getEnrolledStudents(courseId));
    }

    @DeleteMapping("/members/{studentEmail}/courses/{courseId}/")
    public ResponseEntity<?> deleteEnrolledStudent(@PathVariable String studentEmail, @PathVariable String courseId) {
        courseService.deleteEnrolledMember(studentEmail, courseId);
        return ResponseEntity.ok().build();
    }
}
