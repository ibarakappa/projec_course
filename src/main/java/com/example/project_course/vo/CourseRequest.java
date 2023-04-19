package com.example.project_course.vo;

import java.util.List;

import com.example.project_course.entity.Course;
import com.example.project_course.entity.Student;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseRequest {
	@JsonProperty("學號")
	private Integer number;
	@JsonProperty("加選或退選")
	private String addOrDrop;
	@JsonProperty("課程代碼")
	private List<String> courseCodeList;
	@JsonProperty("課程內容")
	private List<Course> courseList;
	@JsonProperty("學生資料")
	private Student student;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public List<String> getCourseCodeList() {
		return courseCodeList;
	}

	public void setCourseCodeList(List<String> courseCodeList) {
		this.courseCodeList = courseCodeList;
	}

	public String getAddOrDrop() {
		return addOrDrop;
	}

	public void setAddOrDrop(String addOrDrop) {
		this.addOrDrop = addOrDrop;
	}

	public List<Course> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

}
