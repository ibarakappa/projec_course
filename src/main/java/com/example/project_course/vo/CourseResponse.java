package com.example.project_course.vo;

import java.util.List;

import com.example.project_course.entity.Course;
import com.example.project_course.entity.Student;
import com.example.project_course.entity.StudentCourse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseResponse {
	@JsonProperty("系統訊息")
	private String message;
	@JsonProperty("課程:")
	private String lesson;
	@JsonProperty("你的課程清單")
	private List<StudentCourse> list;
	@JsonProperty("課程資料")
	private List<Course> courselist;
	@JsonProperty("學生資料")
	private Student student;

	public CourseResponse(String message, Student student) {
		super();
		this.message = message;
		this.student = student;
	}

	public CourseResponse(String message, String lesson) {
		super();
		this.message = message;
		this.lesson = lesson;
	}

	public CourseResponse(String message, List<StudentCourse> list) {
		super();
		this.message = message;
		this.list = list;
	}

	public CourseResponse(String message) {
		super();
		this.message = message;
	}

	public CourseResponse() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<StudentCourse> getList() {
		return list;
	}

	public void setList(List<StudentCourse> list) {
		this.list = list;
	}

	public String getLesson() {
		return lesson;
	}

	public void setLesson(String lesson) {
		this.lesson = lesson;
	}

	public List<Course> getCourselist() {
		return courselist;
	}

	public void setCourselist(List<Course> courselist) {
		this.courselist = courselist;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

}
