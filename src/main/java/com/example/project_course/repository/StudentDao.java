package com.example.project_course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.project_course.entity.Student;

public interface StudentDao extends JpaRepository<Student, Integer> {

	boolean existsByNumber(Integer number);

	Student findByNumber(Integer number);

	@Transactional
	@Modifying
	@Query("update Student s set s.credit = :credit WHERE s.number = :studentId")
	void setStudentCredit(@Param("studentId") Integer number,
			@Param("credit") int credit);
}
