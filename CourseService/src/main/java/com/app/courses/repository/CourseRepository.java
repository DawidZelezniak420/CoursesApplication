package com.app.courses.repository;

import com.app.courses.model.course.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends MongoRepository<Course, Long> {

    Optional<Course> findByCourseId(String courseId);

    List<Course> findAllByStatus(Course.Status status);

    boolean existsByCourseId(String courseId);
}
