package com.example.project_course;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.project_course.repository.CourseDao;
import com.example.project_course.repository.StudentCourseDao;

@SpringBootTest
class ProjectCourseApplicationTests {

	@Autowired
	CourseDao courseDao;
	@Autowired
	StudentCourseDao studentCourseDao;

	@Test
	void contextLoads() {
		System.out.println(courseDao.findCoursebyStudentNumber(1001).size());
	}

}
