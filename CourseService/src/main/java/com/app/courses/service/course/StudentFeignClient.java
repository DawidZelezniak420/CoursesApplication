package com.app.courses.service.course;

import com.app.courses.model.dto.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "STUDENT-SERVICE")
public interface StudentFeignClient {

    @GetMapping("/students/{studentId}/")
    StudentDto getStudentById(@PathVariable Long studentId);


    @PostMapping("/students/by/e-mails/")
    List<StudentDto> getStudentsByEmails(@RequestBody List<String> emails);
}
