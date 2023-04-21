package com.example.project_course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_course.entity.StudentCourse;
import com.example.project_course.entity.StudentCourse.StudentCourseId;

public interface StudentCourseDao extends JpaRepository<StudentCourse, StudentCourseId> {
	List<StudentCourse> findByNumber(int number);

	List<StudentCourse> findByNumberIsAndCourseNameIs(int number, String CourseNameCode);

	StudentCourse findByNumberIsAndCourseCodeIs(int number, String CourseNameCode);

	List<StudentCourse> findByCourseCode(String courseCode);

}
