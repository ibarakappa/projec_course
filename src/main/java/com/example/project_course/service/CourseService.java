package com.example.project_course.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project_course.entity.Course;
import com.example.project_course.vo.CourseRequest;
import com.example.project_course.vo.CourseResponse;
import com.example.project_course.vo.SearchCourseRequest;
import com.example.project_course.vo.SearchCourseResponse;

@Service
public interface CourseService {
	public CourseResponse chooseCourse(CourseRequest req);

	public CourseResponse pickAndDropCourse(CourseRequest req);

	public CourseResponse addNewCourse(CourseRequest req);

//	新增修改功能(課程) 已完成 
	public CourseResponse updateCourse(CourseRequest req);

	public CourseResponse deleteCourse(CourseRequest req);

	public CourseResponse addNewStudent(CourseRequest req);

//	新增修改功能(學生)
	public CourseResponse updateStudent(CourseRequest req);

	public CourseResponse deleteStudent(CourseRequest req);

	public SearchCourseResponse searchStudentCourse(SearchCourseRequest req);

	public SearchCourseResponse searchCourseByCode(SearchCourseRequest req);

	public SearchCourseResponse searchCourseByName(SearchCourseRequest req);

	public List<Course> allCourse();

}
