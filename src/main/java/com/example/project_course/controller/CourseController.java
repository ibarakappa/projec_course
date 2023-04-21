package com.example.project_course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.project_course.service.CourseService;
import com.example.project_course.vo.CourseRequest;
import com.example.project_course.vo.CourseResponse;
import com.example.project_course.vo.SearchCourseRequest;
import com.example.project_course.vo.SearchCourseResponse;

@RestController
public class CourseController {
	@Autowired
	private CourseService courseService;

	@PostMapping("/choose_Course")
	public CourseResponse chooseCourse(@RequestBody CourseRequest req) {
		return courseService.chooseCourse(req);
	}

	@PostMapping("/pick_And_Drop_Course")
	public CourseResponse pickAndDropCourse(@RequestBody CourseRequest req) {
		return courseService.pickAndDropCourse(req);
	}

	@PostMapping("/add_New_Course")
	public CourseResponse addNewCourse(@RequestBody CourseRequest req) {
		return courseService.addNewCourse(req);
	}

	@PostMapping("/update_Course")
	public CourseResponse updateCourse(@RequestBody CourseRequest req) {
		return courseService.updateCourse(req);
	}

	@PostMapping("/delete_Course")
	public CourseResponse deleteCourse(@RequestBody CourseRequest req) {
		return courseService.deleteCourse(req);
	}

	@PostMapping("/add_New_Student")
	public CourseResponse addNewStudent(@RequestBody CourseRequest req) {
		return courseService.addNewStudent(req);
	}

	@PostMapping("/delete_Student")
	public CourseResponse deleteStudent(@RequestBody CourseRequest req) {
		return courseService.deleteStudent(req);
	}

	@PostMapping("/search_Student_Course")
	public SearchCourseResponse searchStudentCourse(
			@RequestBody SearchCourseRequest req) {
		return courseService.searchStudentCourse(req);
	}

	@PostMapping("/search_Course_By_Code")
	public SearchCourseResponse searchCourseByCode(@RequestBody SearchCourseRequest req) {
		return courseService.searchCourseByCode(req);
	}

	@PostMapping("/search_Course_By_Name")
	public SearchCourseResponse searchCourseByName(@RequestBody SearchCourseRequest req) {
		return courseService.searchCourseByName(req);
	}
}
