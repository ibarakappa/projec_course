package com.example.project_course;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.project_course.entity.Course;
import com.example.project_course.repository.CourseDao;

@SpringBootTest
class ProjectCourseApplicationTests {

	@Autowired
	CourseDao courseDao;

	@Test
	void contextLoads() {
		Course lesson = courseDao.findByCourseCode("Ch1");
		var list = (courseDao.findByWeekIsAndStartTimeBetweenOrWeekIsAndEndTimeBetween(
				lesson.getWeek(), lesson.getStartTime(), lesson.getEndTime(),
				lesson.getWeek(), lesson.getStartTime(), lesson.getEndTime()));
		System.out.println(list.size());
	}

}
