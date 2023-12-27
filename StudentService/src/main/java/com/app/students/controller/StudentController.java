package com.app.students.controller;

import com.app.students.model.Student;
import com.app.students.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    @Autowired
    public StudentController(StudentService service) {
        this.service = service;
    }

    //quick add list of students(more info in TestingData class)
    @PostMapping("/add/list/")
    public void addList(@RequestBody List<Student> students) {
        service.addStudentList(students);
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody @Valid Student student) {
        return ResponseEntity.ok().body(service.addStudent(student));
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok().body(service.getAllStudents());
    }

    @GetMapping("/by/status/")
    public ResponseEntity<List<Student>> getStudentsByStatus(@RequestParam(required = false) Student.Status status) {
        return ResponseEntity.ok().body(service.getStudentsByStatus(status));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        return ResponseEntity.ok().body(service.getStudentById(studentId));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> setStudentStatusInactive(@PathVariable Long studentId) {
        service.setStudentAsInactive(studentId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/e-mails/{studentId}")
    public ResponseEntity<Student> updateEmail(@PathVariable Long studentId, @RequestBody @Valid Student student) {
        return ResponseEntity.ok().body(service.putStudent(studentId, student));
    }

    @PatchMapping("/update/{studentId}")
    public ResponseEntity<Student> patchStudent(@PathVariable Long studentId, @RequestBody @Valid Student newStudent) {
        return ResponseEntity.ok().body(service.patchStudent(studentId, newStudent));
    }

    @PostMapping("/by/e-mails")
    public List<Student> getStudentsByEmails(@RequestBody List<String> emails) {
        return service.getStudentsByEmails(emails);
    }
}
