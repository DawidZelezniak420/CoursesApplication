package com.app.courses.controller;

// constants for StudentFrontController class
interface CourseConstants {
    String SUCCESS_ENROLL_MESSAGE = "Student successfully enrolled.";
    String SUCCESS_COURSE_CREATE_MESSAGE = "Course successfully created.";
    String SUCCESS_COURSE_UPDATE_MESSAGE = "Course successfully updated.";
    String REDIRECT_LIST_COURSES = "redirect:/list/courses/";
    String REDIRECT_ENROLLED_MEMBERS = "redirect:/show/enrolled/members/form/?courseId=";
    String UPDATE_COURSE_FORM = "update-course-form";
    String ENROLL_MEMBER_FORM = "enroll-member-form";
    String LIST_COURSES_FORM = "list-courses-form";
    String ENROLLED_MEMBERS_FORM = "enrolled-members-form";
    String COURSE_ATTRIBUTE = "course";
    String ERROR_INFO_ATTRIBUTE = "errorInfo";
    String INFO_ATTRIBUTE = "info";
    String COURSE_ID_ATTRIBUTE = "courseId";
    String ADD_COURSE_FORM = "add-course-form";
    String STUDENT_ATTRIBUTE = "student";
    String MEMBERS_ATTRIBUTE = "members";
    String COURSES_ATTRIBUTE = "courses";

}
