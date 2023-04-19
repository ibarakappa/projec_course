package com.example.project_course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project_course.entity.Course;

public interface CourseDao extends JpaRepository<Course, Integer> {
	Course findByCourseCode(String code);

	boolean existsByCourseCode(String Code);

	List<Course> findByWeekIs(int week);

	List<Course> findByCourseName(String code);
}
