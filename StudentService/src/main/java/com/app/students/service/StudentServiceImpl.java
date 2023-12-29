package com.app.students.service;

import com.app.students.exception.StudentError;
import com.app.students.exception.StudentException;
import com.app.students.model.Student;
import com.app.students.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // quick add list of students
    public void addStudentList(List<Student> students) {
        studentRepository.saveAll(students);
    }

    // you can only add an active student
    public Student addStudent(Student student) {
        validateStudentExists(student.getEmail());
        validateStudentStatus(student.getStatus());
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    //find students by status or get all if status is null
    public List<Student> getStudentsByStatus(Student.Status status) {
        return status != null ? studentRepository.findAllByStatus(status) : studentRepository.findAll();
    }

    public Student getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentException(StudentError.STUDENT_NOT_FOUND));
    }

    // set status to INACTIVE if student is active
    public void setStudentAsInactive(Long studentId) {
        Student student = getStudentById(studentId);
        student.setStatus(Student.Status.INACTIVE);
        studentRepository.save(student);
    }

    public void deleteStudentPermanent(Long studentId) {
        Student studentToDelete = getStudentById(studentId);
        studentRepository.delete(studentToDelete);
    }

    // update all student object
    public Student putStudent(Long studentId, Student student) {
        Student studentFromDb = getStudentById(studentId);
        if (!studentFromDb.getEmail().equals(student.getEmail())
                && studentRepository.existsByEmail(student.getEmail())) {
            throw new StudentException(StudentError.STUDENT_ALREADY_EXISTS);
        }
        setStudentObject(studentFromDb, student);
        studentFromDb.setEmail(student.getEmail());
        return studentRepository.save(studentFromDb);
    }

    // update student without e-mail
    public Student patchStudent(Long studentId, Student newStudent) {
        Student studentFromDb = getStudentById(studentId);
        setStudentObject(studentFromDb, newStudent);
        return studentRepository.save(studentFromDb);
    }

    public List<Student> getStudentsByEmails(List<String> emails) {
        List<Student> students = new ArrayList<>();
        if (!emails.isEmpty()) {
            students = studentRepository.findAllByEmailIn(emails);
        }
        return students;
    }

    private void setStudentObject(Student studentFromDb, Student newStudent) {
        studentFromDb.setFirstName(newStudent.getFirstName());
        studentFromDb.setLastName(newStudent.getLastName());
        studentFromDb.setStatus(newStudent.getStatus());
    }

    // check if student with such email exists
    private void validateStudentExists(String email) {
        if (studentRepository.existsByEmail(email)) {
            throw new StudentException(StudentError.STUDENT_ALREADY_EXISTS);
        }
    }

    private void validateStudentStatus(Student.Status status) {
        if (Student.Status.INACTIVE.equals(status)) {
            throw new StudentException(StudentError.STUDENT_STATUS_IS_INACTIVE);
        }
    }
}
