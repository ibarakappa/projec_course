package com.example.project_course.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.project_course.entity.Course;

public interface CourseDao extends JpaRepository<Course, Integer> {
	Course findByCourseCode(String code);

	boolean existsByCourseCode(String Code);

	List<Course> findByWeekIs(int week);

	List<Course> findByCourseName(String code);

	List<Course> findByWeekIsAndStartTimeBetweenOrWeekIsAndEndTimeBetween(int weekOfStart,
			int startTimeOfStart, int endTimeOfStart, int weekOfEnd, int startTimeOfEnd,
			int endTimeOfEnd);

	@Transactional
	@Modifying
	@Query(value = "select * from course where course_code in(select course_code from student_course where student_number= :number)", nativeQuery = true)
	List<Course> findCoursebyStudentNumber(@Param("number") int studentNumber);
}
