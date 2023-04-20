package com.example.project_course.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchCourseRequest {

	@JsonProperty("學號")
	private Integer number;
	@JsonProperty("課程名稱")
	private String courseName;
	@JsonProperty("課程代碼")
	private String courseCode;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

}
