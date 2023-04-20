package com.example.project_course.vo;

import java.util.List;

import com.example.project_course.entity.Course;
import com.example.project_course.entity.Student;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchCourseResponse {
	@JsonProperty("系統訊息")
	private String message;
	@JsonProperty("學生資料")
	private Student student;
	@JsonProperty("您查詢的所有課程資料")
	private List<Course> list;
	@JsonProperty("課程資料")
	private Course course;

	public SearchCourseResponse() {
		super();
	}

	public SearchCourseResponse(String message) {
		super();
		this.message = message;
	}

	public SearchCourseResponse(String message, Course course) {
		super();
		this.message = message;
		this.course = course;
	}

	public SearchCourseResponse(String message, Student student, List<Course> list) {
		super();
		this.message = message;
		this.student = student;
		this.list = list;
	}

	public SearchCourseResponse(String message, List<Course> list) {
		super();
		this.message = message;
		this.list = list;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public List<Course> getList() {
		return list;
	}

	public void setList(List<Course> list) {
		this.list = list;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

}
