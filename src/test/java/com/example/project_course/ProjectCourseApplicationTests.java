package com.example.project_course;

import java.util.ArrayList;
import java.util.List;

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
		List<String> list = new ArrayList<String>();
		list.add("ABC");
		list.add("CDE");
		list.add("ZZZ");
		System.out.println(list.indexOf("ABC"));
	}

}
