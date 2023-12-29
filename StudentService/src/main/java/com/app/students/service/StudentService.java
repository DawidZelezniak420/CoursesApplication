package com.app.students.service;

import com.app.students.model.Student;

import java.util.List;

public interface StudentService {

    Student addStudent(Student student);

    List<Student> getAllStudents();

    List<Student> getStudentsByStatus(Student.Status status);

    Student getStudentById(Long studentId);

    void setStudentAsInactive(Long studentId);

    Student putStudent(Long studentId, Student student);

    Student patchStudent(Long studentId, Student newStudent);

    List<Student> getStudentsByEmails(List<String> emails);

    void addStudentList(List <Student> students);

    void deleteStudentPermanent(Long studentId);
}
