package com.app.students.controller;

import com.app.students.exception.ErrorInfo;
import com.app.students.exception.StudentException;
import com.app.students.model.Student;
import com.app.students.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

import static com.app.students.controller.StudentConstants.*;

@Controller
public class StudentFrontController {

    private final StudentService service;

    @Autowired
    public StudentFrontController(StudentService service) {
        this.service = service;
    }

    @GetMapping({"/list/students/"})
    public ModelAndView showStudents() {
        ModelAndView mav = new ModelAndView(LIST_STUDENTS_FORM);
        List<Student> allStudents = service.getAllStudents();
        mav.addObject(STUDENTS_ATTRIBUTE, allStudents);
        return mav;
    }

    @GetMapping("/add/students/form/")
    public ModelAndView addStudentForm() {
        ModelAndView modelAndView = new ModelAndView(ADD_STUDENT_FORM);
        Student student = new Student();
        modelAndView.addObject(STUDENT_ATTRIBUTE, student);
        return modelAndView;
    }

    @PostMapping("/save/students/")
    public ModelAndView saveStudent(@ModelAttribute(STUDENT_ATTRIBUTE) @Valid Student student, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView(ADD_STUDENT_FORM);
        ModelAndView errors = getErrors(bindingResult, modelAndView);
        if (errors != null) return errors;
        try {
            service.addStudent(student);
            modelAndView.addObject(INFO_ATTRIBUTE, SUCCESS_CREATING_STUDENT_INFO);
        } catch (StudentException ex) {
            return modelAndView.addObject(ERROR_INFO_ATTRIBUTE, new ErrorInfo(ex.getStudentError().getErrorMessage()));
        }
        return modelAndView;
    }

    @GetMapping("/update/students/form/")
    public ModelAndView showUpdateForm(@RequestParam Long studentId) {
        ModelAndView modelAndView = new ModelAndView(UPDATE_STUDENT_FORM);
        Student student = service.getStudentById(studentId);
        modelAndView.addObject(STUDENT_ATTRIBUTE, student);
        return modelAndView;
    }

    @PostMapping("/update/students/")
    public ModelAndView updateStudent(@RequestParam Long studentId,
                                      @ModelAttribute(STUDENT_ATTRIBUTE) @Valid Student student, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView(UPDATE_STUDENT_FORM);
        ModelAndView errors = getErrors(bindingResult, modelAndView);
        if (errors != null) return errors;
        try {
            service.patchStudent(studentId, student);
            modelAndView.addObject(INFO_ATTRIBUTE, SUCCESS_STUDENT_UPDATE_INFO);
        } catch (StudentException ex) {
            modelAndView.addObject(ERROR_INFO_ATTRIBUTE, new ErrorInfo(ex.getStudentError().getErrorMessage()));
        }
        return modelAndView;
    }

    @GetMapping("/set/status/INACTIVE/")
    public String changeStatusToInactive(@RequestParam Long studentId) {
        service.setStudentAsInactive(studentId);
        return REDIRECT_LIST_STUDENTS;
    }

    @GetMapping("/delete/students/permanent/")
    public String permanentDeleteStudent(@RequestParam Long studentId) {
        service.deleteStudentPermanent(studentId);
        return REDIRECT_LIST_STUDENTS;
    }

    @GetMapping("/update/emails/form/")
    public ModelAndView emailUpdateForm(@RequestParam Long studentId) {
        ModelAndView modelAndView = new ModelAndView(UPDATE_EMAIL_FORM);
        Student student = service.getStudentById(studentId);
        modelAndView.addObject(STUDENT_ATTRIBUTE, student);
        return modelAndView;
    }

    @PostMapping("/update/emails/")
    public ModelAndView updateEmail(@RequestParam Long studentId,
                                    @ModelAttribute(STUDENT_ATTRIBUTE) @Valid Student student, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView(UPDATE_EMAIL_FORM);
        ModelAndView errors = getErrors(bindingResult, modelAndView);
        if (errors != null) return errors;
        try {
            service.putStudent(studentId, student);
            modelAndView.addObject(INFO_ATTRIBUTE, SUCCESS_EMAIL_UPDATE_INFO);
        } catch (StudentException ex) {
            modelAndView.addObject(ERROR_INFO_ATTRIBUTE, new ErrorInfo(ex.getStudentError().getErrorMessage()));
        }
        return modelAndView;
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
