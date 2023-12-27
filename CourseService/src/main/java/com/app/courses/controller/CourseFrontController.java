package com.app.courses.controller;

import com.app.courses.exception.CourseException;
import com.app.courses.exception.ErrorInfo;
import com.app.courses.model.course.Course;
import com.app.courses.model.course.CourseMember;
import com.app.courses.model.dto.StudentDto;
import com.app.courses.service.course.CourseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

import static com.app.courses.controller.CourseConstants.*;

@Controller
@Slf4j
public class CourseFrontController {

    private final CourseService service;

    @Autowired
    public CourseFrontController(CourseService service) {
        this.service = service;
    }

    @GetMapping({"/list/courses/"})
    public ModelAndView showCourses() {
        ModelAndView modelAndView = new ModelAndView(LIST_COURSES_FORM);
        List<Course> allCourses = service.getAllCourses();
        modelAndView.addObject(COURSES_ATTRIBUTE, allCourses);
        return modelAndView;
    }

    @GetMapping("/add/courses/form/")
    public ModelAndView addCourseForm() {
        ModelAndView modelAndView = new ModelAndView(ADD_COURSE_FORM);
        Course course = new Course();
        modelAndView.addObject(COURSE_ATTRIBUTE, course);
        return modelAndView;
    }

    @PostMapping("/save/courses/")
    public ModelAndView saveCourse(@ModelAttribute(COURSE_ATTRIBUTE) @Valid Course course, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView(ADD_COURSE_FORM);
        ModelAndView errors = getErrors(bindingResult, modelAndView);
        if (errors != null) return errors;
        try {
            service.addCourse(course);
            modelAndView.addObject(INFO_ATTRIBUTE, SUCCESS_COURSE_CREATE_MESSAGE);
        } catch (CourseException ex) {
            return modelAndView.addObject(ERROR_INFO_ATTRIBUTE, new ErrorInfo(ex.getCourseError().getErrorMessage()));
        }
        return modelAndView;
    }

    @GetMapping("/update/courses/form/")
    public ModelAndView showUpdateForm(@RequestParam String courseId) {
        ModelAndView modelAndView = new ModelAndView(UPDATE_COURSE_FORM);
        Course course = service.getCourseById(courseId);
        modelAndView.addObject(COURSE_ATTRIBUTE, course);
        return modelAndView;
    }

    @PostMapping("/update/courses/")
    public ModelAndView updateCourse(@RequestParam String courseId,
                                     @ModelAttribute(COURSE_ATTRIBUTE) @Valid Course course, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView(UPDATE_COURSE_FORM);
        ModelAndView errors = getErrors(bindingResult, modelAndView);
        if (errors != null) return errors;
        try {
            service.patchCourse(courseId, course);
            modelAndView.addObject(INFO_ATTRIBUTE, SUCCESS_COURSE_UPDATE_MESSAGE);
        } catch (CourseException ex) {
            modelAndView.addObject(ERROR_INFO_ATTRIBUTE, new ErrorInfo(ex.getCourseError().getErrorMessage()));
        }
        return modelAndView;
    }

    @GetMapping("/delete/courses/")
    public String deleteCourse(@RequestParam String courseId) {
        service.deleteCourse(courseId);
        return REDIRECT_LIST_COURSES;
    }

    @GetMapping("/add/members/form/")
    public ModelAndView enrollMemberForm(@RequestParam String courseId) {
        ModelAndView modelAndView = new ModelAndView(ENROLL_MEMBER_FORM);
        modelAndView.addObject(COURSE_ID_ATTRIBUTE, courseId);
        modelAndView.addObject(STUDENT_ATTRIBUTE, new StudentDto());
        return modelAndView;
    }

    @PostMapping("/add/members/")
    public ModelAndView enrollMember(@ModelAttribute(STUDENT_ATTRIBUTE) StudentDto student,
                                      @RequestParam String courseId, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView(ENROLL_MEMBER_FORM);
        modelAndView.addObject(COURSE_ID_ATTRIBUTE, courseId);
        ModelAndView errors = getErrors(bindingResult, modelAndView);
        if (errors != null) return errors;
        try {
            service.enrollStudent(student.getStudentId(), courseId);
            modelAndView.addObject(INFO_ATTRIBUTE, SUCCESS_ENROLL_MESSAGE);
        } catch (CourseException ex) {
            modelAndView.addObject(ERROR_INFO_ATTRIBUTE, new ErrorInfo(ex.getCourseError().getErrorMessage()));
        }
        return modelAndView;
    }

    @GetMapping("/show/enrolled/members/form/")
    public ModelAndView showEnrolledMembersForm(@RequestParam String courseId) {
        ModelAndView modelAndView = new ModelAndView(ENROLLED_MEMBERS_FORM);
        Course course = service.getCourseById(courseId);
        List<CourseMember> courseMembers = course.getCourseMembers();
        modelAndView.addObject(COURSE_ATTRIBUTE, course);
        modelAndView.addObject(MEMBERS_ATTRIBUTE, courseMembers);
        return modelAndView;
    }

    @GetMapping("/delete/members/")
    public String deleteMember(@RequestParam String memberEmail, @RequestParam String courseId) {
        service.deleteEnrolledMember(memberEmail, courseId);
        return REDIRECT_ENROLLED_MEMBERS + courseId;
    }

    @GetMapping("/finish/enroll/")
    public String finishEnrollAndSendNotifications(@RequestParam String courseId) {
        service.finishEnroll(courseId);
        return REDIRECT_LIST_COURSES;
    }

    private static ModelAndView getErrors(BindingResult bindingResult, ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            List<ErrorInfo> errorInfos = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .map(ErrorInfo::new)
                    .collect(Collectors.toList());
            return modelAndView.addObject(ERROR_INFO_ATTRIBUTE, errorInfos);
        }
        return null;
    }
}
